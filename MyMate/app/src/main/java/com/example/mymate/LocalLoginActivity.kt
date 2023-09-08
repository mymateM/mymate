package com.example.mymate

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.contentColorFor
import com.example.mymate.databinding.ActivityLocalloginBinding
import java.util.regex.Pattern

class LocalLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocalloginBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalloginBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        binding.localjoinbtn.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        //TODO: pwd 조건 따라서 inputfilter 정규식 적용

    }

}