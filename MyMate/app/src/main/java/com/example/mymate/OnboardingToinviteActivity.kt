package com.example.mymate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityOnboardingToinviteBinding

class OnboardingToinviteActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingToinviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        binding = ActivityOnboardingToinviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.inviteCode.setOnClickListener {
            val invitecode = binding.inviteCode.text
            val clip = ClipData.newPlainText("inviteCode", invitecode)
            clipboard.setPrimaryClip(clip)
        }

        binding.copypastebtn.setOnClickListener {
            val invitecode = binding.inviteCode.text
            val clip = ClipData.newPlainText("inviteCode", invitecode)
            clipboard.setPrimaryClip(clip)
        }

        binding.copypasteView.setOnClickListener {
            val invitecode = binding.inviteCode.text
            val clip = ClipData.newPlainText("inviteCode", invitecode)
            clipboard.setPrimaryClip(clip)
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingHouseholdActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, R.anim.vertical_exit)
        }
    }
}