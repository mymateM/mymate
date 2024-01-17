package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityLocaljoinpwdBinding
import java.util.regex.Pattern

class LocalJoinPwdActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var binding: ActivityLocaljoinpwdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocaljoinpwdBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)
        var message = ""
        var checkmessage = ""
        val patternEN = "^(.*)[A-Za-z0-9](.*)$"
        val patternEngNum = Pattern.compile(patternEN)
        val patternsp = "^(.*)[$@$!%*#?&.](.*)$"
        val patternSpecial = Pattern.compile(patternsp)
        var pwdshow = false
        var pwdcheck = false

        val email = intent.getStringExtra("email")
        binding.clearpwd.isGone = true
        binding.clearpwdcheck.isGone = true

        binding.clearpwd.setOnClickListener {
            binding.pwd.text = null
        }

        binding.clearpwdcheck.setOnClickListener {
            binding.pwdchecktext.text = null
        }

        binding.pwd.setOnClickListener {
            binding.pwd.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleblue_select)
            binding.pwdchecktext.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
        }

        binding.pwdchecktext.setOnClickListener {
            binding.pwdchecktext.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleblue_select)
            binding.pwd.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
        }

        binding.pwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = binding.pwd.text.toString()
                binding.clearpwd.isGone = message.isEmpty()
                if (message.isEmpty()) {
                    binding.pwd.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
                } else {
                    binding.pwd.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleblue_select)
                }
                val matcherEngNum = patternEngNum.matcher(message)
                val matcherSpecial = patternSpecial.matcher(message)
                if (message.length > 7) {
                    minimumcheck(true)
                    engnumcheck(matcherEngNum.find())
                    spcheck(matcherSpecial.find())
                } else {
                    minimumcheck(false)
                    engnumcheck(matcherEngNum.find())
                    spcheck(matcherSpecial.find())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                message = binding.pwd.text.toString()
                val matcherEngNum = patternEngNum.matcher(message)
                val matcherSpecial = patternSpecial.matcher(message)
                if (message.length > 7 && matcherEngNum.find() && matcherSpecial.find()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (!pwdshow) {
                            pwdshow = true
                            showcheck()
                        }
                    }, 300)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (pwdshow) {
                            hidecheck()
                            pwdshow = false
                        }
                    }, 300)
                }
            }

        })

        binding.pwdchecktext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkmessage = binding.pwdchecktext.text.toString()
                message = binding.pwd.text.toString()
                binding.clearpwdcheck.isGone = message.isEmpty()
                if (message.isEmpty()) {
                    binding.pwdchecktext.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
                } else {
                    binding.pwdchecktext.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleblue_select)
                }
                if (checkmessage == message) {
                    hidekeyboard()
                    binding.pwdcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                    binding.pwdcheckdesc.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                    binding.joinbtn.isEnabled = true
                    binding.joinbtn.setBackgroundResource(R.drawable.button_loginbarselected)
                } else {
                    binding.pwdcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                    binding.pwdcheckdesc.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
                    binding.joinbtn.isEnabled = false
                    binding.joinbtn.setBackgroundResource(R.drawable.button_loginbardefault)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.back.setOnClickListener {
            finish()
        }

        binding.joinbtn.setOnClickListener {
            startActivity(Intent(context, OnboardingProfileActivity::class.java))
        }
    }

    fun minimumcheck(check: Boolean) {
        if (check) {
            binding.minimumcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
            binding.minimumchecktext.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
        } else {
            binding.minimumcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
            binding.minimumchecktext.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
        }
    }

    fun engnumcheck(check: Boolean) {
        if (check) {
            binding.engnumcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
            binding.engnumchecktext.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
        } else {
            binding.engnumcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
            binding.engnumchecktext.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
        }
    }

    fun spcheck(check: Boolean) {
        if(check) {
            binding.spcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
            binding.spchecktext.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
        } else {
            binding.spcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
            binding.spchecktext.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
        }
    }

    fun showcheck() {
        binding.minimumcheck.isGone = true
        binding.minimumchecktext.isGone = true
        binding.engnumcheck.isGone = true
        binding.engnumchecktext.isGone = true
        binding.spcheck.isGone = true
        binding.spchecktext.isGone = true
        binding.pwdchecktext.isGone = false
        binding.pwdcheckindi.isGone = false
        binding.pwdcheck.isGone = false
        binding.pwdcheckdesc.isGone = false
        binding.joinbtn.isGone = false
    }

    fun hidecheck() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.minimumcheck.isGone = false
            binding.minimumchecktext.isGone = false
        }, 300)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.engnumcheck.isGone = false
            binding.engnumchecktext.isGone = false
        }, 100)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.spcheck.isGone = false
            binding.spchecktext.isGone = false
        }, 100)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.pwdchecktext.isGone = true
            binding.pwdcheckindi.isGone = true
            binding.pwdcheck.isGone = true
            binding.pwdcheckdesc.isGone = true
            binding.joinbtn.isEnabled = false
            binding.joinbtn.isGone = true
            binding.joinbtn.setBackgroundResource(R.drawable.button_loginbardefault)
        }, 100)
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
        binding.pwd.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
        binding.pwdchecktext.backgroundTintList = ContextCompat.getColorStateList(context, R.color.graylight_buttonline)
    }
}