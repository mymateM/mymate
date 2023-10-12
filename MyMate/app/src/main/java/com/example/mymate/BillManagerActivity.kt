package com.example.mymate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityBillmanagerBinding

class BillManagerActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillmanagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBillmanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.gasview.setOnClickListener {
            //TODO: to list page
        }
        binding.electronicview.setOnClickListener {
            //TODO: to list page
        }
        binding.waterview.setOnClickListener {
            //TODO: to list page
        }
        binding.etcview.setOnClickListener {
            //TODO: to list page
        }
        binding.addbtn.setOnClickListener {
            //TODO: to add page
            //TODO: Modale
            startActivity(Intent(this, BillCameraActivity::class.java))
        }
    }
}