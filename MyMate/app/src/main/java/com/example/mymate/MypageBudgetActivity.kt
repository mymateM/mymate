package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityMypageBudgetBinding

class MypageBudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageBudgetBinding
    lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        val budgetText = SpannableStringBuilder(intent.getStringExtra("budget"))
        binding.amountText.text = budgetText
        binding.descheader.isGone = true
        binding.desc.isGone = true

        budgetText.setSpan(TypefaceSpan(montBoldTypeface), 0, budgetText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        budgetText.setSpan(TypefaceSpan(suitBoldTypeface), budgetText.length - 1, budgetText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

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

        binding.amountEdit.setOnClickListener {
            val editIntent = Intent(context, MypageEditbudgetActivity::class.java)
            editIntent.putExtra("budget", binding.amountText.text.substring(0 until binding.amountText.text.length - 1))
            startActivity(editIntent)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}