package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivityOnboardingGetinviteBinding

class OnboardingGetinviteActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingGetinviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingGetinviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextbtn.isEnabled = false

        binding.back.setOnClickListener {
            hidekeyboard()
            finish()
        }

        binding.getinviteview.setOnClickListener {
            hidekeyboard()
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingCreatedActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, R.anim.vertical_exit)
        }

        binding.inviteEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inviteEdit.text.toString().length == 6) {
                    binding.nextbtn.isEnabled = true
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                } else {
                    binding.nextbtn.isEnabled = false
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfdefault)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.inviteEdit.onFocusChangeListener = OnFocusChangeListener { p0, p1 ->
            if (p1) {
                binding.inviteEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purpleblue_select)
            } else {
                binding.inviteEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.graylight_basic)
            }
        }
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}