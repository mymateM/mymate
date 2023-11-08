package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputFilter
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.contentColorFor
import com.example.mymate.databinding.ActivityLocalloginBinding
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.regex.Pattern

class LocalLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocalloginBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    private var loginResponse: localLoginResponse = localLoginResponse()
    private var deviceresponse: deviceTokenResponse = deviceTokenResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalloginBinding.inflate(layoutInflater)
        context = this
        userRepo = DataStoreRepoUser(dataStore)
        setContentView(binding.root)

        //retrofit code
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localLogin::class.java)
        var deviceendpoint = retrofit?.create(localDevice::class.java)

        binding.localaccquestion.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.localaccquestion.setOnClickListener {
            startActivity(Intent(context, OnboardingTermsActivity::class.java))
        }
        //TODO: pwd 조건 따라서 inputfilter 정규식 적용
        binding.localloginbutton.setOnClickListener{
            //로그인 처리 통신
            var email = binding.localloginid.text.toString()
            var password = binding.localloginpwd.text.toString()

            var userData = loginUser(email, password)

            var fcm = MyFirebaseMessagingService()
            var accessToken = ""
            var devicebearer = devicetoken(fcm.getFirebaseToken())
            Log.d("devicebearer", devicebearer.deviceToken)

            endpoint!!.localLogin(userData).enqueue(object: Callback<localLoginResponse> {
                override fun onResponse(
                    call: Call<localLoginResponse>,
                    response: Response<localLoginResponse>
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
                    deviceendpoint!!.localDevice("Bearer " + accessToken, devicebearer).enqueue(object : Callback<Response<Void>> {
                        override fun onResponse(
                            call: Call<Response<Void>>,
                            response: Response<Response<Void>>
                        ) {
                            Log.d("devicetoken", "success")
                        }

                        override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                            Log.d("devicetoken", "failed")
                        }


                    })
                    startActivity(Intent(context, MainActivity::class.java))
                }
                override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                    Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT)
                }
            })

            //FirebaseMessaging.getInstance().token
            //fcm.sendFirebaseToken()

        }

    }

}