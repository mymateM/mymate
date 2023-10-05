package com.example.mymate

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityLocaljoinBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocalJoinActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var binding: ActivityLocaljoinBinding
    lateinit var userRepo: DataStoreRepoUser

    private var joinResponse: localLoginResponse = localLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocaljoinBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        //retrofit code
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localRegister::class.java)
        userRepo = DataStoreRepoUser(dataStore)

        //TODO: EditText clear Button, 버튼활성화 등

        var pwdbox = binding.joinpwdvalue
        var pwdcheckbox = binding.joinpwdcheck
        var joinbutton = binding.localjoinbutton
        var checktext = binding.pwdchecktext

        pwdcheckbox.addTextChangedListener(object : TextWatcher {
            //비밀번호 일치 확인 처리코드
            override fun afterTextChanged(p0: Editable?) {
                if(pwdbox.text.toString().equals(pwdcheckbox.text.toString())) {
                    joinbutton.isEnabled=true
                    checktext.text = "비밀번호가 일치합니다."
                    checktext.setTextColor(Color.GREEN)
                    //TODO: button tint. tint 코드 미발견으로 색상 따로 drawable 쓸 것
                    //TODO: 비밀번호 조건 확인하고 일치할 시 애니메이션 적용, 단 조금 기다리게 하여 조건 재확인할 필요성 있음 - 리니어 레이아웃이 더 좋을지도?
                } else {
                    checktext.text = "비밀번호가 일치하지 않습니다."
                    checktext.setTextColor(Color.RED)
                    joinbutton.isEnabled=false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(pwdbox.text.toString().equals(pwdcheckbox.text.toString())) {
                    joinbutton.isEnabled=true
                    checktext.text = "비밀번호가 일치합니다."
                    checktext.setTextColor(Color.GREEN)
                    //TODO: button tint. tint 코드 미발견으로 색상 따로 drawable 쓸 것
                } else {
                    checktext.text = "비밀번호가 일치하지 않습니다."
                    checktext.setTextColor(Color.RED)
                    joinbutton.isEnabled=false
                }
            }
        })

        joinbutton.setOnClickListener {
            //회원가입 처리 통신
            var email = binding.joinidvalue.text.toString()
            var pwd = binding.joinpwdvalue.text.toString()

            var loginInfo = loginUser(email, pwd)

            Log.i("nickname", loginInfo.email)

            endpoint!!.localRegister(loginInfo).enqueue(object: Callback<localLoginResponse> {
                override fun onResponse(call: Call<localLoginResponse>, response: Response<localLoginResponse>) {
                    joinResponse = response.body()!!
                    CoroutineScope(IO).launch {
                        userRepo.keyUser(joinResponse.data.access_token.toString(), joinResponse.data.refresh_token.toString())
                        Log.i("accesscode", joinResponse.data.access_token.toString())
                        Log.i("refreshcode", joinResponse.data.refresh_token.toString())
                    }
                    var fcm = MyFirebaseMessagingService()
                    fcm.sendFirebaseToken()
                    startActivity(Intent(context, MainActivity::class.java))
                }

                override fun onFailure(call: Call<localLoginResponse>, t: Throwable) {
                    Toast.makeText(context, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT)
                }
            })
        }
    }

}