package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySettingBinding

class SettingActivity: AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.invitecode.setOnClickListener {
            startActivity(Intent(context, SettingInvitecodeActivity::class.java))
            overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.toinvitecode.setOnClickListener {
            startActivity(Intent(context, SettingInvitecodeActivity::class.java))
            overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.pwdchange.setOnClickListener {
            sorry()
        }

        binding.topwdchange.setOnClickListener {
            sorry()
        }

        binding.faq.setOnClickListener {
            sorry()
        }

        binding.tofaq.setOnClickListener {
            sorry()
        }

        binding.logout.setOnClickListener {
            notSorry()
        }

        binding.tologout.setOnClickListener {
            notSorry()
        }

        binding.signout.setOnClickListener {
            notSorry()
        }

        binding.tosignout.setOnClickListener {
            notSorry()
        }

        binding.security.setOnClickListener {
            sorry()
        }

        binding.tosecurity.setOnClickListener {
            sorry()
        }

        binding.theme.setOnClickListener {
            sorry()
        }

        binding.totheme.setOnClickListener {
            sorry()
        }

        binding.tonewversion.setOnClickListener {
            sorry()
        }
    }

    fun notSorry() {
        Toast.makeText(context, "전시로 인해 로그아웃/회원탈퇴가 불가합니다.", Toast.LENGTH_SHORT).show()
    }

    fun sorry() {
        Toast.makeText(context, "준비중이에요!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}