package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.data.dto.auth.DeviceToken
import com.example.mymate.data.dto.auth.request.LocalLoginRequest
import com.example.mymate.data.dto.auth.response.LocalLoginResponse
import com.example.mymate.data.remote.service.RetrofitClientInstance
import com.example.mymate.databinding.ActivityLocalloginBinding
import com.example.mymate.presentation.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocalLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocalloginBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    private var loginResponse: LocalLoginResponse = LocalLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalloginBinding.inflate(layoutInflater)
        context = this
        userRepo = DataStoreRepoUser(dataStore)
        setContentView(binding.root)

        //retrofit code
        /* var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localLogin::class.java)
        var deviceendpoint = retrofit?.create(localDevice::class.java) */

        binding.localaccquestion.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.localaccquestion.setOnClickListener {
            startActivity(Intent(context, OnboardingTermsActivity::class.java))
        }
        //TODO: pwd 조건 따라서 inputfilter 정규식 적용
        binding.localloginbutton.setOnClickListener{
            //로그인 처리 통신
            var email = binding.localloginid.text.toString()
            var password = binding.localloginpwd.text.toString()

            var userData = LocalLoginRequest(email, password)

            var fcm = MyFirebaseMessagingService()
            var accessToken = ""
            var devicebearer = DeviceToken(fcm.getFirebaseToken())
            Log.d("devicebearer", devicebearer.deviceToken)

            /* endpoint!!.localLogin(userData).enqueue(object: Callback<LocalLoginResponse> {
                override fun onResponse(
                    call: Call<LocalLoginResponse>,
                    response: Response<LocalLoginResponse>
                ) {
                    loginResponse = response.body()!!
                    runBlocking {
                        var userPUT = launch {
                            userRepo.keyUser(loginResponse.data.access_token.toString(), loginResponse.data.refresh_token.toString())
                            Log.i("accesscode", loginResponse.data.access_token.toString())
                            Log.i("refreshcode", loginResponse.data.refresh_token.toString())
                        }
                        userPUT.join()
                        var userGET = launch {
                            accessToken = userRepo.userAccessReadFlow.first().toString()
                        }
                        userGET.join()
                        Log.d("getaccesscode", accessToken)
                    }
                    fcm.sendFirebaseToken()
                    deviceendpoint!!.localDevice("Bearer $accessToken", devicebearer).enqueue(object : Callback<Response<Void>> {
                        override fun onResponse(
                            call: Call<Response<Void>>,
                            response: Response<Response<Void>>
                        ) {
                            Log.d("devicetoken", "success")
                            startActivity(Intent(context, MainActivity::class.java))
                        }

                        override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                            Log.d("devicetoken", "failed")
                            startActivity(Intent(context, MainActivity::class.java))
                        }
                    })
                }
                override fun onFailure(call: Call<LocalLoginResponse>, t: Throwable) {
                    Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }) */

            //FirebaseMessaging.getInstance().token
            //fcm.sendFirebaseToken()

        }

    }

}