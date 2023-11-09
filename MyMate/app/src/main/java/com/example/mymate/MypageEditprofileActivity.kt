package com.example.mymate

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityMypageEditprofileBinding

class MypageEditprofileActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditprofileBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageEditprofileBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        val nickname = intent.getStringExtra("nickname")
        binding.nickname.text = nickname

        binding.backbtn.setOnClickListener {
            finish()
        }
    }
}