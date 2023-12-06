package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        val toptext = SpannableStringBuilder("돈을 정산하고 싶은]\n날짜를 선택해 주세요")
        toptext.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.purpleblue_select)), 11, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.topText.text = toptext

        binding.nextbtn.isEnabled = false
        binding.dayText.isGone = true

        binding.settlementdayview.setOnClickListener {
            hidekeyboard()
        }

        firstday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            binding.nextbtn.isEnabled = true
            binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            hidekeyboard()
        }

        fifteenthday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
            endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            binding.nextbtn.isEnabled = true
            binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            hidekeyboard()
        }

        endday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
            customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
            binding.nextbtn.isEnabled = true
            binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            hidekeyboard()
        }

        customday.setOnClickListener {
            if (message.isNotEmpty()) {
                firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
                binding.nextbtn.isEnabled = true
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                binding.dayText.isGone = false
                binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
                binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(applicationContext, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        customtext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                message = customtext.text.toString()
                if (message.isNotEmpty()) {
                    if (message == "29" || message == "30" || message == "31") {
                        firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
                        customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        Toast.makeText(applicationContext, "설정하신 날짜가 29일, 30일, 31일 중 하나일 경우 자동으로 말일로 변경됩니다.", Toast.LENGTH_SHORT).show()
                        binding.nextbtn.isEnabled = true
                        binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                        binding.dayText.isGone = false
                        hidekeyboard()
                    } else if (message.toInt() > 31) {
                        Toast.makeText(applicationContext, "유효한 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        customtext.text.clear()
                    } else {
                        firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                        customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectedbox))
                        binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.nextbtn.isEnabled = true
                        binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                        binding.dayText.isGone = false
                    }
                } else {
                    firstday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                    fifteenthday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                    endday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                    customday.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.button_selectbox))
                    binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.nextbtn.isEnabled = false
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfdefault)
                    binding.dayText.isGone = true
                }
            }
        })

        binding.nextbtn.setOnClickListener {
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