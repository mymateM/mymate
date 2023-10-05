package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ActivityOnboardingAccountBinding

class OnboardingAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bankbutton = binding.bankSelect
        bankbutton.setOnClickListener {
            val modaleView = OnboardingAccountModaleFragment()
            modaleView.show(supportFragmentManager, modaleView.tag)
        }
        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
        }
        binding.nextbtn.setOnClickListener {
            //TODO: 여기서 정보 저장
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
        }
        binding.accountview.setOnClickListener {
            hidekeyboard()
        }
        binding.editcontainer.setOnClickListener {
            binding.accountEdit.requestFocus()
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.accountEdit, 0)
        }

        //TODO: 정보형 확정나면 adapter 작성
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}