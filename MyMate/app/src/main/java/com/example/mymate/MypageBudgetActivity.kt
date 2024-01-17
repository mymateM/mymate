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
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.isDigitsOnly
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityMypageBudgetBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class MypageBudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageBudgetBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser
    lateinit var montBoldTypeface: Typeface
    lateinit var suitBoldTypeface: Typeface

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        binding.descheader.isGone = true
        binding.desc.isGone = true
        binding.allowbox.isGone = true

        montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)

        val viewitem = ArrayList<TextView>()
        viewitem.add(binding.allownone)
        viewitem.add(binding.allow5)
        viewitem.add(binding.allow10)
        viewitem.add(binding.allow15)
        viewitem.add(binding.allow20)

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyBudget::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.getMyBudget("Bearer $accessToken").enqueue(object : Callback<myBudgetResponse> {
            override fun onResponse(
                call: Call<myBudgetResponse>,
                response: Response<myBudgetResponse>
            ) {
                Log.d("budgets", response.message())
                if (response.isSuccessful) {
                    val budget = response.body()!!.data

                    val budgetText = SpannableStringBuilder(digitprocessing(budget.budget_amount) + "원")
                    budgetText.setSpan(TypefaceSpan(montBoldTypeface), 0, budgetText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    budgetText.setSpan(TypefaceSpan(suitBoldTypeface), budgetText.length - 1, budgetText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    binding.amountText.text = budgetText

                    binding.amountEdit.setOnClickListener {
                        val editIntent = Intent(context, MypageEditbudgetActivity::class.java)
                        editIntent.putExtra("budget", budget.budget_amount)
                        editIntent.putExtra("allowance", budget.budget_allowance)
                        startActivityForResult(editIntent, 90)
                    }

                    when (budget.budget_allowance.toInt()) {
                        0 -> binding.allownone.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        5 -> binding.allow5.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        10 -> binding.allow10.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        15 -> binding.allow15.setBackgroundResource(R.drawable.button_selectedboxradi32)
                        20 -> binding.allow20.setBackgroundResource(R.drawable.button_selectedboxradi32)
                    }

                    when (budget.budget_allowance.toInt()) {
                        0 -> binding.allownone.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                        5 -> binding.allow5.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                        10 -> binding.allow10.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                        15 -> binding.allow15.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                        20 -> binding.allow20.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                    }

                    var allowint = 0.0
                    var allowtxt = SpannableStringBuilder("원만큼 더 썼어요")
                    val montMediumTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_medium), Typeface.NORMAL)

                    binding.allownone.setOnClickListener {
                        select(binding.allownone, viewitem)
                        allowint = 0.0
                        allowtxt = SpannableStringBuilder("${allowint.toInt() / 10000}만원까지는 더 지출해도 괜찮아요")
                        allowtxt.setSpan(TypefaceSpan(montMediumTypeface), 0, (allowint.toInt() / 10000).toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.allowbox.text = allowtxt
                        binding.allowpercent.text = "0%"
                    }

                    binding.allow5.setOnClickListener {
                        select(binding.allow5, viewitem)
                        allowint = binding.amountText.text.substring(0, binding.amountText.text.lastIndex).replace(",", "").toDouble() * 0.05
                        allowtxt = SpannableStringBuilder("${allowint.toInt() / 10000}만원까지는 더 지출해도 괜찮아요")
                        allowtxt.setSpan(TypefaceSpan(montMediumTypeface), 0, (allowint.toInt() / 10000).toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.allowbox.text = allowtxt
                        binding.allowpercent.text = "5%"
                    }

                    binding.allow10.setOnClickListener {
                        select(binding.allow10, viewitem)
                        allowint = binding.amountText.text.substring(0, binding.amountText.text.lastIndex).replace(",", "").toDouble() * 0.10
                        allowtxt = SpannableStringBuilder("${allowint.toInt() / 10000}만원까지는 더 지출해도 괜찮아요")
                        allowtxt.setSpan(TypefaceSpan(montMediumTypeface), 0, (allowint.toInt() / 10000).toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.allowbox.text = allowtxt
                        binding.allowpercent.text = "10%"
                    }

                    binding.allow15.setOnClickListener {
                        select(binding.allow15, viewitem)
                        allowint = binding.amountText.text.substring(0, binding.amountText.text.lastIndex).replace(",", "").toDouble() * 0.15
                        allowtxt = SpannableStringBuilder("${allowint.toInt() / 10000}만원까지는 더 지출해도 괜찮아요")
                        allowtxt.setSpan(TypefaceSpan(montMediumTypeface), 0, (allowint.toInt() / 10000).toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.allowbox.text = allowtxt
                        binding.allowpercent.text = "15%"
                    }

                    binding.allow20.setOnClickListener {
                        select(binding.allow20, viewitem)
                        allowint = binding.amountText.text.substring(0, binding.amountText.text.lastIndex).replace(",", "").toDouble() * 0.20
                        allowtxt = SpannableStringBuilder("${allowint.toInt() / 10000}만원까지는 더 지출해도 괜찮아요")
                        allowtxt.setSpan(TypefaceSpan(montMediumTypeface), 0, (allowint.toInt() / 10000).toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.allowbox.text = allowtxt
                        binding.allowpercent.text = "20%"
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

        binding.back.setOnClickListener {
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

    private fun select(item: TextView, list: ArrayList<TextView>) {
        binding.allowbox.isGone = false
        for (i in 0 until list.size) {
            list[i].setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
            list[i].setBackgroundResource(R.drawable.button_selectboxradi32)
        }
        for (i in 0 until list.size) {
            if (item == list[i]) {
                item.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                item.setBackgroundResource(R.drawable.button_selectedboxradi32)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 90) {
                val budget = data?.getStringExtra("budget")
                if (!budget.isNullOrBlank() && budget.isDigitsOnly()) {
                    val budgetText = SpannableStringBuilder(digitprocessing(budget) + "원")
                    budgetText.setSpan(TypefaceSpan(montBoldTypeface), 0, budgetText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    budgetText.setSpan(TypefaceSpan(suitBoldTypeface), budgetText.length - 1, budgetText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.amountText.text = budgetText
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }
}