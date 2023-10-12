package com.example.mymate

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.UserManager
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.mymate.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

class LoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var context: Context

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var userRepoUser: DataStoreRepoUser

    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    lateinit var socialAccesscode: String

    override fun onStart() {
        super.onStart()
    }

    //TODO: 로그인 성공 후 main으로 넘겨줄 때 현재 액티비티를 포함한 다른 액티비티 스택 클리어

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)
        //datastore setting
        userRepoUser = DataStoreRepoUser(dataStore)
        var accesscode = ""
        var refreshcode = ""
        //socialLogin setting
        socialAccesscode = "null"
        //retrofit setting
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(socialLogin::class.java)
        //kakao login
        val kakaologin = binding.kakaologin
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                endpoint!!.soicalLogin(socialUserLogin("KAKAO", token.accessToken)).enqueue(object: Callback<localLoginResponse>{
                    override fun onResponse(
                        call: Call<localLoginResponse>,
                        response: Response<localLoginResponse>
                    ) {
                        var socialResponse = response.body()!!
                        runBlocking {
                            var putacin = launch {
                                userRepoUser.keyUser(socialResponse.data.access_token, socialResponse.data.refresh_token)
                            }
                            putacin.join()
                        }
                        Log.d("socialLogin KAKAO", socialResponse.data.access_token)
                        startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                    override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                        Toast.makeText(context, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                })
                // TODO: 신규 계정 등록시 온보딩으로 연결
            }
        }
        kakaologin.setOnClickListener{
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        endpoint!!.soicalLogin(socialUserLogin("KAKAO", token.accessToken)).enqueue(object: Callback<localLoginResponse>{
                            override fun onResponse(
                                call: Call<localLoginResponse>,
                                response: Response<localLoginResponse>
                            ) {
                                var socialResponse = response.body()!!
                                runBlocking {
                                    var putacin = launch {
                                        userRepoUser.keyUser(socialResponse.data.access_token, socialResponse.data.refresh_token)
                                    }
                                    putacin.join()
                                }
                                Log.d("socialLogin KAKAO", socialResponse.data.access_token)
                                startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            }
                            override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                                Toast.makeText(context, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                            }
                        })
                        //TODO: 신규 계정 등록시 온보딩으로 연결
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
        //google login
        val googlelogin = binding.googlelogin
        setResultSignUp()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestServerAuthCode(getString(R.string.google_web_client_id))
            .requestIdToken(getString(R.string.google_web_client_id))
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        googlelogin.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            resultLauncher.launch(signInIntent)
        }
        //naver login
        binding.naverlogin.setOnClickListener {
            val oauthLoginCallback = object  : OAuthLoginCallback {
                override fun onError(errorCode: Int, message: String) {
                    Log.e(NaverIdLoginSDK.getLastErrorCode().toString(), NaverIdLoginSDK.getLastErrorDescription().toString())
                    Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e(NaverIdLoginSDK.getLastErrorCode().toString(), NaverIdLoginSDK.getLastErrorDescription().toString())
                    Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess() {
                    Log.i("네이버 로그인", "login success: ${NaverIdLoginSDK.getAccessToken().toString()}")
                    endpoint!!.soicalLogin(socialUserLogin("NAVER", NaverIdLoginSDK.getAccessToken().toString())).enqueue(object: Callback<localLoginResponse>{
                        override fun onResponse(
                            call: Call<localLoginResponse>,
                            response: Response<localLoginResponse>
                        ) {
                            var socialResponse = response.body()!!
                            runBlocking {
                                var putacin = launch {
                                    userRepoUser.keyUser(socialResponse.data.access_token, socialResponse.data.refresh_token)
                                    Log.d("socialLogin NAVER", socialResponse.data.access_token)
                                }
                                putacin.join()
                            }
                            Log.d("socialLogin NAVER", socialResponse.data.access_token)
                            startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        }
                        override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                            Toast.makeText(context, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

            }
            NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
        }
        //local login
        val locallogin = binding.locallogin
        locallogin.setOnClickListener{
            startActivity(Intent(this, LocalLoginActivity::class.java))
        }
    }

    private fun setResultSignUp() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e(TAG, "resultlauncher 진행됨")
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val authcode = account?.serverAuthCode.toString()
            val idtoken = account?.idToken.toString()

            Log.d("GOOGLE", idtoken)

            var retrofit = RetrofitClientInstance.client
            var endpoint = retrofit?.create(socialLogin::class.java)

            endpoint!!.soicalLogin(socialUserLogin("GOOGLE", idtoken)).enqueue(object: Callback<localLoginResponse>{
                override fun onResponse(
                    call: Call<localLoginResponse>,
                    response: Response<localLoginResponse>
                ) {
                    var socialResponse = response.body()!!
                    runBlocking {
                        var putacin = launch {
                            userRepoUser.keyUser(socialResponse.data.access_token, socialResponse.data.refresh_token)
                        }
                        putacin.join()
                    }
                    Log.d("socialLogin GOOGLE", socialResponse.data.access_token)
                    startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
                override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                    Toast.makeText(context, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code" + e.localizedMessage)
        }
    }

    private fun sendSocialCode(type: String, code: String) {
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(socialLogin::class.java)
        endpoint!!.soicalLogin(socialUserLogin(type, code)).enqueue(object: Callback<localLoginResponse>{
            override fun onResponse(
                call: Call<localLoginResponse>,
                response: Response<localLoginResponse>
            ) {
                var socialResponse = response.body()!!
                runBlocking {
                    var putacin = launch {
                        userRepoUser.keyUser(socialResponse.data.access_token, socialResponse.data.refresh_token)
                    }
                    putacin.join()
                }
                Log.d("socialLogin function $type", socialResponse.data.access_token)
                startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                Toast.makeText(context, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}