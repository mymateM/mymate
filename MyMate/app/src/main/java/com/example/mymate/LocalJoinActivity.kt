package com.example.mymate

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityLocaljoinBinding

class LocalJoinActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var binding: ActivityLocaljoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocaljoinBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        //TODO: EditText clear Button, pwd 일치율 체크, 버튼활성화 등
    }

}