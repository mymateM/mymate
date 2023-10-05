package com.example.mymate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityOnboardingInviteBinding

class OnboardingInviteActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingInviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.inviteBtn.setOnClickListener {
            startActivity(Intent(this, OnboardingToinviteActivity::class.java))
        }

        binding.invitedBtn.setOnClickListener {
            startActivity(Intent(this, OnboardingGetinviteActivity::class.java))
        }
    }
}