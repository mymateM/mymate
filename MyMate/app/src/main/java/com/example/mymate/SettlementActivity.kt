package com.example.mymate

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySettlementBinding

class SettlementActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettlementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettlementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val modalebutton = binding.modalepop
        modalebutton.setOnClickListener{
            val modaleView = SettlementModaleFragment()
            modaleView.show(supportFragmentManager, modaleView.tag)
        }
        binding.settlementdismiss.setOnClickListener{
            finish()
        }
    }
}