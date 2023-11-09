package com.example.mymate

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityMypageAccountBinding

class MypageAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageAccountBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this


    }
}