package com.example.mymate

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityMypageEditratioBinding

class MypageEditratioActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditratioBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageEditratioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

    }
}