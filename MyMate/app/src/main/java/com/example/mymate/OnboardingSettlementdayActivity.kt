package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityOnboardingSettlementdayBinding
import kotlinx.coroutines.selects.select

class OnboardingSettlementdayActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingSettlementdayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingSettlementdayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firstday = binding.settleDay1
        val fifteenthday = binding.settleDay15
        val endday = binding.settleDayEnd
        val customday = binding.settleDayEdit
        val customtext = binding.settleDayEditText
        var message = ""

        binding.nextbtn.isEnabled = false
        binding.dayText.isGone = true

        binding.settlementdayview.setOnClickListener {
            hidekeyboard()
        }

        firstday.setOnClickListener {
            firstday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
            fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            endday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            customday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            binding.nextbtn.isEnabled = true
            hidekeyboard()
        }

        fifteenthday.setOnClickListener {
            firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            fifteenthday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
            endday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            customday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            binding.nextbtn.isEnabled = true
            hidekeyboard()
        }

        endday.setOnClickListener {
            firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            endday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
            customday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
            binding.nextbtn.isEnabled = true
            hidekeyboard()
        }

        customday.setOnClickListener {
            if (message.isNotEmpty()) {
                firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                endday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                customday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
                binding.nextbtn.isEnabled = true
                binding.dayText.isGone = false
            } else {
                Toast.makeText(applicationContext, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        customtext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                message = customtext.text.toString()
                if (message.isNotEmpty()) {
                    if (message == "29" || message == "30" || message == "31") {
                        firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        endday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
                        customday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        Toast.makeText(applicationContext, "설정하신 날짜가 29일, 30일, 31일 중 하나일 경우 자동으로 말일로 변경됩니다.", Toast.LENGTH_SHORT).show()
                        binding.nextbtn.isEnabled = true
                        binding.dayText.isGone = false
                        hidekeyboard()
                    } else if (message.toInt() > 31) {
                        Toast.makeText(applicationContext, "유효한 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        customtext.text.clear()
                    } else {
                        firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        endday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                        customday.setImageDrawable(getDrawable(R.drawable.icon_defaultrectangle))
                        binding.nextbtn.isEnabled = true
                        binding.dayText.isGone = false
                    }
                } else {
                    firstday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                    fifteenthday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                    endday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                    customday.setImageDrawable(getDrawable(R.drawable.box_modalelist))
                    binding.nextbtn.isEnabled = false
                    binding.dayText.isGone = true
                }
            }
        })

        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementbudgetActivity::class.java))
        }

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingSettlementbudgetActivity::class.java))
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