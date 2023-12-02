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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.example.mymate.databinding.ActivityOnboardingSettlementbudgetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.roundToInt

class OnboardingSettlementbudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingSettlementbudgetBinding
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingSettlementbudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toptext = SpannableStringBuilder("우리 집의 예산을 정해보세요")
        toptext.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.purpleblue_select)), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.topText.text = toptext

        val budgetEdit = binding.settlebudget
        var message = ""

        bottomSheetInit()

        binding.nextbtn.isEnabled = false
        binding.nextbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingCreatedActivity::class.java))
        }

        binding.backbtn.setOnClickListener {
            finish()
        }

        budgetEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = budgetEdit.text.toString()
                binding.nextbtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) { }

        })
    }

    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.settlepersistent.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true

        var budgetText = "0만원까지는 더 써도 괜찮아요"
        var allowbudget = 0.0
        var budgetround = 0
        var selection = ""
        val allowpercentage = binding.settleallow

        select(selection, budgetText)

        binding.settlementbudgetview.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            hidekeyboard()
        }

        binding.settleallow.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            hidekeyboard()
        }

        binding.settlebudget.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.settlebudget.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.settlebudget.text.isNotEmpty()) {
                    binding.settleallow.isEnabled =
                        binding.settlebudget.text.toString().toInt() != 0
                } else {
                    binding.settleallow.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        //TODO: textspan

        binding.settlepersistent.allownone.setOnClickListener {
            //TODO: editText 숫자인지 확인. editText 숫자제한 코드를 xml에 넣을 것. maxlines = 1, digits = 숫자만.
            if (binding.settlebudget.text.isNotEmpty()) {
                budgetText = "0만원까지는 더 써도 괜찮아요"
                select("none", budgetText)
                allowpercentage.text = "0%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow5.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty()) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.05
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow5", budgetText)
                allowpercentage.text = "5%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow10.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty()) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.1
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow10", budgetText)
                allowpercentage.text = "10%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }

            hidekeyboard()
        }

        binding.settlepersistent.allow15.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty()) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.15
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow15", budgetText)
                allowpercentage.text = "15%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }

            hidekeyboard()
        }

        binding.settlepersistent.allow20.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty()) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.2
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow20", budgetText)
                allowpercentage.text = "20%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }
            hidekeyboard()
        }
    }

    private fun select(selection: String, budgetText: String) {
        if (selection == "allow5") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectedboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
        } else if (selection == "allow10") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectedboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
        } else if (selection == "allow15") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectedboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
        } else if (selection == "allow20") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectedboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
        } else if (selection == "none") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectedboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
        } else {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.button_selectboxradi32)
            binding.settlepersistent.allownone.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow5.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow10.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow15.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
            binding.settlepersistent.allow20.setTextColor(ContextCompat.getColor(applicationContext, R.color.graydark_text))
        }

        binding.settleallowbudget.text = budgetText
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}