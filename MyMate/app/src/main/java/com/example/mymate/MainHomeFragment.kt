package com.example.mymate

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainHomeFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainHomeFragment : Fragment() {
    lateinit var binding: MainHomeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var userRepo: DataStoreRepoUser

    lateinit var refreshcode: String
    lateinit var accesscode: String

    private var refreshResponse: localRefreshReponse = localRefreshReponse()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainHomeFragmentBinding.inflate(inflater, container, false)
        val alarmIntent = Intent(mainActivity, AlarmActivity::class.java)
        binding.alarmbutton.setOnClickListener{startActivity(alarmIntent)}

        //retrofit code
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localRefresh::class.java)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)
        val templogin = binding.logoimage
        templogin.setOnClickListener {
            UserApiClient.instance.logout { error -> //카카오 로그아웃
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
            val account = GoogleSignIn.getLastSignedInAccount(mainActivity)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(getString(R.string.google_web_client_id))
                .requestIdToken(getString(R.string.google_web_client_id))
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(mainActivity, gso)
            account?.let {
                Log.i(TAG, "Logged In")
                mGoogleSignInClient.signOut() //구글 로그아웃
            } ?: Log.i(TAG, "Not Yet Logged In")
            try {
                NaverIdLoginSDK.logout()
                Log.i(TAG, "로그아웃 성공, SDK에서 네이버 토큰 삭제됨")
            } finally {
                Log.i(TAG, "로그아웃 실패, SDK에 네이버 토큰 없음")
            } // 네이버 로그아웃
            startActivity(Intent(mainActivity, LoginActivity::class.java))
        }

        val temprefresh = binding.homecharacter
        temprefresh.setOnClickListener {
            runBlocking {
                /*var initrefresh = launch {
                    userRepo.userRefreshReadFlow.collect {
                        refreshcode = it.toString()
                        Log.i("refreshinit", refreshcode)
                        cancel()
                    }
                }
                initrefresh.join()*/
                refreshcode = userRepo.userRefreshReadFlow.first().toString()
                Log.i("initfirst", refreshcode)
            }
            /*endpoint!!.localRefresh("Bearer " + refreshcode).enqueue(object: Callback<localRefreshReponse> {
                override fun onResponse(
                    call: Call<localRefreshReponse>,
                    response: Response<localRefreshReponse>
                ) {
                    refreshResponse = response.body()!!
                    Log.i("routine start", "routine start")
                    runBlocking {
                        var userPUT = launch {
                            userRepo.keyUser(refreshResponse.access_token.toString(), refreshResponse.refresh_token.toString())
                            Log.i("accesscode", refreshResponse.access_token.toString())
                            Log.i("refreshcode", refreshResponse.refresh_token.toString())
                            cancel()
                        }
                        userPUT.join()
                    }
                    Log.i("routine over", "routine over")
                }
                override fun onFailure(call: Call<localRefreshReponse>, t: Throwable) {
                    Toast.makeText(mainActivity, "갱신 실패.", Toast.LENGTH_SHORT)
                }
            })*/
        }

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}