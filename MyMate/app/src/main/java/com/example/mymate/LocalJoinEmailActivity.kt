package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityLocaljoinemailBinding

class LocalJoinEmailActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var binding: ActivityLocaljoinemailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocaljoinemailBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)
        binding.clear.isGone = true

        binding.tonext.isEnabled = false

        binding.root.setOnClickListener {
            hidekeyboard()
        }

        binding.emailcontainer.setOnClickListener {
            binding.email.post(Runnable {
                binding.email.requestFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.email.findFocus(), InputMethodManager.SHOW_IMPLICIT)
            })
        }

        binding.clear.setOnClickListener {
            binding.email.text = null
        }

        var message = ""
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = binding.email.text.toString()
                binding.clear.isGone = message.isEmpty()
                if (binding.email.text.isNotEmpty() && message.contains("@")) {
                    binding.tonext.isEnabled = true
                    binding.tonext.setBackgroundResource(R.drawable.button_loginbarselected)
                } else {
                    binding.tonext.isEnabled = false
                    binding.tonext.setBackgroundResource(R.drawable.button_loginbardefault)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.tonext.setOnClickListener {
            val intent = Intent(context, LocalJoinPwdActivity::class.java)
            intent.putExtra("email", binding.email.text.toString())
            startActivity(intent)
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