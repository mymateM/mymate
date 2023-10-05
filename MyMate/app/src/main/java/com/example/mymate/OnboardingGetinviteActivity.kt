package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityOnboardingGetinviteBinding

class OnboardingGetinviteActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingGetinviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingGetinviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            hidekeyboard()
            finish()
        }

        binding.getinviteview.setOnClickListener {
            hidekeyboard()
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingHouseholdActivity::class.java))
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