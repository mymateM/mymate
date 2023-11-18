package com.example.mymate

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.mymate.databinding.ActivityMypageAccountBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageAccountBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyAccount::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.getMyAccount("Bearer $accessToken").enqueue(object : Callback<myAccountResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<myAccountResponse>,
                response: Response<myAccountResponse>
            ) {
                if (response.isSuccessful) {
                    val myaccount = response.body()!!.data
                    val banktype = myaccount.account_bank.replace("은행", "")
                    val banknumber = myaccount.account_number
                    val fullspan = SpannableStringBuilder("$banktype $banknumber")
                    fullspan.setSpan(TypefaceSpan(suitBoldTypeface), 0, banktype.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    fullspan.setSpan(TypefaceSpan(montBoldTypeface), banktype.length, fullspan.lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.account.text = fullspan
                }
            }

            override fun onFailure(call: Call<myAccountResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(사용자 계좌)", Toast.LENGTH_SHORT).show()
            }

        })

        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}