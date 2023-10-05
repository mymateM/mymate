package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityOnboardingHouseholdBinding

class OnboardingHouseholdActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingHouseholdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingHouseholdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
        }

        binding.householdview.setOnClickListener {
            hidekeyboard()
        }
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}