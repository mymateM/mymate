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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LocalLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocalloginBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    private var loginResponse: localLoginResponse = localLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalloginBinding.inflate(layoutInflater)
        context = this
        userRepo = DataStoreRepoUser(dataStore)
        setContentView(binding.root)

        //retrofit code
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localLogin::class.java)

        binding.localjoinbtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.localjoinbtn.setOnClickListener {
            startActivity(Intent(context, LocalJoinActivity::class.java))
        }
        //TODO: pwd 조건 따라서 inputfilter 정규식 적용
        binding.localloginbutton.setOnClickListener{
            //로그인 처리 통신
            var email = binding.localloginid.text.toString()
            var password = binding.localloginpwd.text.toString()

            var userData = loginUser(email, password)

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
                            startActivity(Intent(context, MainActivity::class.java))
                        }
                        userPUT.join()
                    }
                }
                override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                    Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT)
                }
            })
        }

    }

}