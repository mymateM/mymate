package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivityOnboardingHouseholdBinding

class OnboardingHouseholdActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingHouseholdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingHouseholdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextbtn.isEnabled = false
        val postpond = SpannableStringBuilder("나중에 설정할게요")
        postpond.setSpan(UnderlineSpan(), 0, postpond.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.contbtn.text = postpond

        val toptext = SpannableStringBuilder("우리 집, 이름을 지어봐요!\n어떻게 부르면 될까요?")
        toptext.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.purpleblue_select)), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.topText.text = toptext

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementdayActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.householdview.setOnClickListener {
            hidekeyboard()
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.housenameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.housenameEdit.text.isNotEmpty()) {
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

        binding.housenameEdit.onFocusChangeListener =
            OnFocusChangeListener { p0, p1 ->
                if (p1) {
                    binding.housenameEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purpleblue_select)
                } else {
                    binding.housenameEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.graylight_basic)
                }
            }

        binding.housenameEdit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_DONE || p1 == EditorInfo.IME_ACTION_GO) {
                    hidekeyboard()
                }
                return false
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}