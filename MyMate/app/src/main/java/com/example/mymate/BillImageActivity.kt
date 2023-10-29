package com.example.mymate

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityBillimageBinding

class BillImageActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillimageBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillimageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
    }
}