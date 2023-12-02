package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivityOnboardingHouseholdBinding

class OnboardingHouseholdActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingHouseholdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingHouseholdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextbtn.isEnabled = false

        val toptext = SpannableStringBuilder("우리 집, 이름을 지어봐요!\n어떻게 부르면 될까요?")
        toptext.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.purpleblue_select)), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.topText.text = toptext

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
        }

        binding.householdview.setOnClickListener {
            hidekeyboard()
        }

        binding.housenameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.housenameEdit.text.isNotEmpty()) {
                    binding.nextbtn.isEnabled = true
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                } else {
                    binding.nextbtn.isEnabled = false
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfdefault)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}