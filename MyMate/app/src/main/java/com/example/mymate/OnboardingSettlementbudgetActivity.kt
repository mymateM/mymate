package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
                if (message.isNotEmpty()) {
                    binding.nextbtn.isEnabled = true
                } else {
                    binding.nextbtn.isEnabled = false
                }
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

        binding.settlepersistent.modaledismiss.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

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

        //TODO: textspan

        binding.settlepersistent.allownone.setOnClickListener {
            //TODO: editText 숫자인지 확인. editText 숫자제한 코드를 xml에 넣을 것. maxlines = 1, digits = 숫자만.
            budgetText = "0만원까지는 더 써도 괜찮아요"
            select("none", budgetText)
            allowpercentage.text = "0%"
            hidekeyboard()
        }

        binding.settlepersistent.allow5.setOnClickListener {
            allowbudget = binding.settlebudget.text.toString().toDouble() * 0.05
            budgetround = allowbudget.roundToInt()
            budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
            select("allow5", budgetText)
            allowpercentage.text = "5%"
            hidekeyboard()
        }

        binding.settlepersistent.allow10.setOnClickListener {
            allowbudget = binding.settlebudget.text.toString().toDouble() * 0.1
            budgetround = allowbudget.roundToInt()
            budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
            select("allow10", budgetText)
            allowpercentage.text = "10%"
            hidekeyboard()
        }

        binding.settlepersistent.allow15.setOnClickListener {
            allowbudget = binding.settlebudget.text.toString().toDouble() * 0.15
            budgetround = allowbudget.roundToInt()
            budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
            select("allow15", budgetText)
            allowpercentage.text = "15%"
            hidekeyboard()
        }

        binding.settlepersistent.allow20.setOnClickListener {
            allowbudget = binding.settlebudget.text.toString().toDouble() * 0.2
            budgetround = allowbudget.roundToInt()
            budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
            select("allow20", budgetText)
            allowpercentage.text = "20%"
            hidekeyboard()
        }
    }

    private fun select(selection: String, budgetText: String) {
        if (selection == "allow5") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalebutton)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
        } else if (selection == "allow10") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalebutton)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
        } else if (selection == "allow15") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalebutton)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
        } else if (selection == "allow20") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalebutton)
        } else if (selection == "none") {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalebutton)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
        } else {
            binding.settlepersistent.allownone.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow5.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow10.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow15.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
            binding.settlepersistent.allow20.background = ContextCompat.getDrawable(this, R.drawable.box_modalelist)
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