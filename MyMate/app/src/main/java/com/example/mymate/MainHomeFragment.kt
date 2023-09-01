package com.example.mymate

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainHomeFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient

class MainHomeFragment : Fragment() {
    lateinit var binding: MainHomeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var mGoogleSignInClient: GoogleSignInClient

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
            startActivity(Intent(mainActivity, LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}