package com.example.mymate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.mymate.databinding.ActivityBillocrBinding

class BillOCRActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillocrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillocrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedPath = intent.getStringExtra("savedUri")
        val moneyPath = intent.getStringExtra("savedMoney")
        val dayPath = intent.getStringExtra("savedDay")
        binding.previewImage.setImageURI(savedPath?.toUri())
        binding.billtemp.setImageURI(moneyPath?.toUri())
        binding.moneytemp.setImageURI(dayPath?.toUri())
    }
}