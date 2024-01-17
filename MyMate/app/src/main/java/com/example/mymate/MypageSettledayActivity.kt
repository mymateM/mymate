package com.example.mymate

import android.content.Context
import android.content.Intent
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
import com.example.mymate.databinding.ActivityMypageSettledayBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class MypageSettledayActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageSettledayBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageSettledayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)
        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(myPageApi::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.myPageApi("Bearer $accessToken").enqueue(object : Callback<myPageApiResponse> {
            override fun onResponse(
                call: Call<myPageApiResponse>,
                response: Response<myPageApiResponse>
            ) {
                if (response.isSuccessful) {
                    val day = response.body()!!.data.household_settlement_date
                    val daytext = SpannableStringBuilder("매월 ${day}일")
                    daytext.setSpan(TypefaceSpan(montBoldTypeface), 3, daytext.lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    daytext.setSpan(TypefaceSpan(suitBoldTypeface), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    daytext.setSpan(TypefaceSpan(suitBoldTypeface), daytext.lastIndex, daytext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.day.text = daytext

                    binding.dateEdit.setOnClickListener {
                        /*val editIntent = Intent(context, MypageEditdayActivity::class.java)
                        editIntent.putExtra("day", day)
                        startActivity(editIntent)*/
                        Toast.makeText(context, "전시로 인해 정산일 변경이 불가합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<myPageApiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(마이페이지-정산일)", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(myPageApi::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.myPageApi("Bearer $accessToken").enqueue(object : Callback<myPageApiResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<myPageApiResponse>,
                response: Response<myPageApiResponse>
            ) {
                if (response.isSuccessful) {
                    val day = response.body()!!.data.household_settlement_date
                    val daytext = SpannableStringBuilder("매월 ${day}일")
                    val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
                    val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
                    daytext.setSpan(TypefaceSpan(montBoldTypeface), 3, daytext.lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    daytext.setSpan(TypefaceSpan(suitBoldTypeface), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    daytext.setSpan(TypefaceSpan(suitBoldTypeface), daytext.lastIndex, daytext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.day.text = daytext

                    binding.dateEdit.setOnClickListener {
                        /*val editIntent = Intent(context, MypageEditdayActivity::class.java)
                        editIntent.putExtra("day", day)
                        startActivity(editIntent)*/
                        Toast.makeText(context, "전시로 인해 정산일 변경이 불가합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<myPageApiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(마이페이지-정산일)", Toast.LENGTH_SHORT).show()
            }

        })
    }
}