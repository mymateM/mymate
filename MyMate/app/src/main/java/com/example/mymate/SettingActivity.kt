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
        }

        binding.toinvitecode.setOnClickListener {
            startActivity(Intent(context, SettingInvitecodeActivity::class.java))
        }


    }
}