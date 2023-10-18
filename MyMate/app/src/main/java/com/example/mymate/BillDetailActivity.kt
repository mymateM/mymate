package com.example.mymate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityBilldetailBinding

class BillDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilldetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilldetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("itemname")
        val day = intent.getStringExtra("itemday")
        val amount = intent.getStringExtra("itemamount")
        val category = intent.getStringExtra("category")

        Log.d("오씨알 디테일", day + "")

        binding.billamount.text = amount
        binding.category.text = category
        binding.duedate.text = day
        binding.company.text = name

        binding.backbtn.setOnClickListener {
            val backintent = Intent(this, BillManagerListActivity::class.java)
            backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            backintent.putExtra("category", category)
            startActivity(backintent)
            overridePendingTransition(0, 0)
        }
    }

    override fun onBackPressed() {
        val backintent = Intent(this, BillManagerListActivity::class.java)
        val category = intent.getStringExtra("category")
        backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        backintent.putExtra("category", category)
        startActivity(backintent)
        overridePendingTransition(0, 0)
    }
}