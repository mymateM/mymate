package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityMypageBudgetBinding
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageBudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageBudgetBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        binding.descheader.isGone = true
        binding.desc.isGone = true

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyBudget::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.getAccessToken().first().toString()
        }

        endpoint!!.getMyBudget("Bearer $accessToken").enqueue(object : Callback<myBudgetResponse> {
            override fun onResponse(
                call: Call<myBudgetResponse>,
                response: Response<myBudgetResponse>
            ) {
                Log.d("budgets", response.message())
                if (response.isSuccessful) {
                    val budget = response.body()!!.data

                    val budgetText = SpannableStringBuilder(budget.budget_amount + "원")
                    budgetText.setSpan(TypefaceSpan(montBoldTypeface), 0, budgetText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    budgetText.setSpan(TypefaceSpan(suitBoldTypeface), budgetText.length - 1, budgetText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    binding.amountText.text = budgetText

                    binding.amountEdit.setOnClickListener {
                        val editIntent = Intent(context, MypageEditbudgetActivity::class.java)
                        editIntent.putExtra("budget", budget.budget_amount)
                        editIntent.putExtra("allowance", budget.budget_allowance)
                        startActivity(editIntent)
                    }

                    when (budget.budget_allowance.toInt()) {
                        0 -> binding.allownone.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        5 -> binding.allow5.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        10 -> binding.allow10.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        15 -> binding.allow15.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        20 -> binding.allow20.setBackgroundResource(R.drawable.button_selectedboxradi32)
                    }

                    val allowance = budget.budget_allowance + "%"
                    binding.allowpercent.text = allowance
                } else {
                    Toast.makeText(context, "반환형 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<myBudgetResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(마이페이지-예산)", Toast.LENGTH_SHORT).show()
            }

        })

        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.budgetview.setOnClickListener {
            binding.descheader.isGone = true
            binding.desc.isGone = true
        }

        binding.about.setOnClickListener {
            if (!binding.descheader.isGone) {
                binding.descheader.isGone = true
                binding.desc.isGone = true
            } else {
                binding.descheader.isGone = false
                binding.desc.isGone = false
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}