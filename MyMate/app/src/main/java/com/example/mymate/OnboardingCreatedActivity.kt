package com.example.mymate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityOnboardingCreatedBinding

class OnboardingCreatedActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingCreatedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingCreatedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toHome.setOnClickListener {
            startActivity(Intent(this, OnboardingProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnboardingProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}