package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityMypageEditbudgetBinding

class MypageEditbudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditbudgetBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageEditbudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.budget.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("budget")?.replace(",", ""))
        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.completedbtn.setOnClickListener {
            finish()
        }
    }
}