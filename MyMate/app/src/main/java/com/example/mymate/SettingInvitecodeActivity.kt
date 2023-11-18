package com.example.mymate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySettingInvitecodeBinding

class SettingInvitecodeActivity: AppCompatActivity() {
    lateinit var binding: ActivitySettingInvitecodeBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingInvitecodeBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        binding.copypasteView.setOnClickListener {
            val invitecode = binding.inviteCode.text
            val clip = ClipData.newPlainText("inviteCode", invitecode)
            clipboard.setPrimaryClip(clip)
        }

        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}