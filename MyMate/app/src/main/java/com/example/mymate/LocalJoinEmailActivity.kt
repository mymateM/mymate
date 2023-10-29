package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityLocaljoinemailBinding

class LocalJoinEmailActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var binding: ActivityLocaljoinemailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocaljoinemailBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)

        binding.tonext.isEnabled = false

        var message = ""
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = binding.email.text.toString()
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
}