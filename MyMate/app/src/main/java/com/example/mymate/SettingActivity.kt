package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySettingBinding

class SettingActivity: AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.toinvitecode.setOnClickListener {
            startActivity(Intent(context, SettingInvitecodeActivity::class.java))
            overridePendingTransition(R.anim.right_enter, R.anim.none)
        }


    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}