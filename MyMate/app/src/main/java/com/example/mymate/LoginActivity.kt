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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.sign

class LoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var context: Context

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var userRepoUser: DataStoreRepoUser

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account == null) {
            Log.e("Google account", "로그인 안 되어 있음")
        } else {
            Log.e("Google account", "로그인 완료된 상태")
        }
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
        //kakao login
        val kakaologin = binding.kakaologin
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                CoroutineScope(IO).launch { //datastore
                    userRepoUser.keyUser(token.accessToken, "heyheyhey")
                    userRepoUser.userAccessReadFlow.collect {
                        accesscode = it.toString()
                        Log.i("accesscode", accesscode)
                    }
                }
                CoroutineScope(IO).launch {
                    userRepoUser.userRefreshReadFlow.collect {
                        refreshcode = it.toString()
                        Log.i("refreshcode", refreshcode)
                    }
                }
                startActivity(Intent(this, MainActivity::class.java))// TODO: 신규 계정 등록시 온보딩으로 연결
                //TODO: send accessToken to server
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
                        CoroutineScope(IO).launch {//datastore
                            userRepoUser.keyUser(token.accessToken, "heyheyhey")
                            userRepoUser.userAccessReadFlow.collect {
                                accesscode = it.toString()
                                Log.i("accesscode", accesscode)
                            }
                        }
                        CoroutineScope(IO).launch {
                            userRepoUser.userRefreshReadFlow.collect {
                                refreshcode = it.toString()
                                Log.i("refreshcode", refreshcode)
                            }
                        }
                        startActivity(Intent(this, MainActivity::class.java)) //TODO: 신규 계정 등록시 온보딩으로 연결
                    //TODO: send accessToken to server
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
        //local login
        val locallogin = binding.locallogin
        locallogin.setOnClickListener{
            val account = GoogleSignIn.getLastSignedInAccount(context)
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

            CoroutineScope(IO).launch {
                userRepoUser.keyUser(authcode, "null")
            }

            startActivity(Intent(this, MainActivity::class.java))
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code" + e.localizedMessage)
        }
    }
}