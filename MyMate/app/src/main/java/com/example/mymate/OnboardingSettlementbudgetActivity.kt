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
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
        toptext.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.purpleblue_select)), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.topText.text = toptext

        val postpond = SpannableStringBuilder("나중에 설정할게요")
        postpond.setSpan(UnderlineSpan(), 0, postpond.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.contbtn.text = postpond
        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingCreatedActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        val budgetEdit = binding.settlebudget
        var message = ""

        bottomSheetInit()

        binding.nextbtn.isEnabled = false
        binding.nextbtn.setOnClickListener {
            if (binding.settleallow.text == "허용 오차 범위") {
                Toast.makeText(this, "허용 오차 범위를 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, OnboardingCreatedActivity::class.java))
                overridePendingTransition(R.anim.none, R.anim.none)
            }
        }

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        /*budgetEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = budgetEdit.text.toString()
                binding.nextbtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })*/

        budgetEdit.onFocusChangeListener = OnFocusChangeListener { p0, p1 ->
            if (p1) {
                budgetEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purpleblue_select)
            } else {
                budgetEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.graylight_basic)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.none)
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

        binding.settlepersistent.root.setOnClickListener {

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

        binding.settlebudget.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.settlebudget.text.isNotEmpty()) {
                    if (binding.settlebudget.text.toString().toInt() == 0) {
                        Toast.makeText(applicationContext, "0원은 예산으로 입력할 수 없어요!", Toast.LENGTH_SHORT).show()
                        hidekeyboard()
                        binding.settlebudget.text.clear()
                    } else {
                        binding.nextbtn.isEnabled = true
                    }
                } else {
                    binding.nextbtn.isEnabled = false
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfdefault)
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        //TODO: textspan

        binding.settlepersistent.allownone.setOnClickListener {
            //TODO: editText 숫자인지 확인. editText 숫자제한 코드를 xml에 넣을 것. maxlines = 1, digits = 숫자만.
            if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
                budgetText = "0만원까지는 더 써도 괜찮아요"
                select("none", budgetText)
                allowpercentage.text = "0%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(this, "먼저 예산을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow5.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.05
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow5", budgetText)
                allowpercentage.text = "5%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(this, "먼저 예산을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow10.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.1
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow10", budgetText)
                allowpercentage.text = "10%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(this, "먼저 예산을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow15.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.15
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow15", budgetText)
                allowpercentage.text = "15%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(this, "먼저 예산을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        binding.settlepersistent.allow20.setOnClickListener {
            if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.2
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow20", budgetText)
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.text = "20%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else {
                Toast.makeText(this, "먼저 예산을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }
    }

    private fun cal(selection: String) {
        var budgetText = "0만원까지는 더 써도 괜찮아요"
        var allowbudget = 0.0
        var budgetround = 0
        val allowpercentage = binding.settleallow
        if (binding.settlebudget.text.isNotEmpty() && binding.settlebudget.text.toString().toInt() != 0) {
            if (selection == "0%") {
                budgetText = "0만원까지는 더 써도 괜찮아요"
                select("none", budgetText)
                allowpercentage.text = "0%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else if (selection == "5%") {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.05
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow5", budgetText)
                allowpercentage.text = "5%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else if (selection == "10%") {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.1
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow10", budgetText)
                allowpercentage.text = "10%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else if (selection == "15%") {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.15
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow15", budgetText)
                allowpercentage.text = "15%"
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            } else if (selection == "20%") {
                allowbudget = binding.settlebudget.text.toString().toDouble() * 0.2
                budgetround = allowbudget.roundToInt()
                budgetText = "" + budgetround + "만원까지는 더 써도 괜찮아요"
                select("allow20", budgetText)
                binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                allowpercentage.text = "20%"
                allowpercentage.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            }
        } else {
            binding.nextbtn.setBackgroundResource(R.drawable.button_wfdefault)
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
        if (binding.settleallow.text != "허용 오차 범위") {
            cal(binding.settleallow.text.toString())
        }
    }
}