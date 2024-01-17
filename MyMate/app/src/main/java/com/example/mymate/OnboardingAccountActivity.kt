package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ActivityOnboardingAccountBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.checkerframework.common.subtyping.qual.Bottom

class OnboardingAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingAccountBinding
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private val imgList = ArrayList<Drawable>()
    private val nameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextbtn.isEnabled = false
        val postpond = SpannableStringBuilder("나중에 설정할게요")
        postpond.setSpan(UnderlineSpan(), 0, postpond.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.contbtn.text = postpond

        bottomSheetInit()

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.nextbtn.setOnClickListener {
            //TODO: 여기서 정보 저장
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.accountview.setOnClickListener {
            hidekeyboard()
        }

        binding.accountEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.accountEdit.text.isNotEmpty() && binding.bankName.text != "은행") {
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

        binding.accountEdit.onFocusChangeListener = OnFocusChangeListener { p0, p1 ->
            if (p1) {
                binding.accountEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purpleblue_select)
            } else {
                binding.accountEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.graylight_basic)
            }
        }

        //TODO: 정보형 확정나면 adapter 작성
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.none)
    }

    private fun listset() {
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_busan_gyungnam)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_kb)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_ibk)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_nh)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_daegu)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_citi)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_shinhan)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_woori)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_woochaeguk)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_sc)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_jeju)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_kakao)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_toss)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_hana)!!)
        
        nameList.add("경남")
        nameList.add("KB국민")
        nameList.add("IBK기업")
        nameList.add("NH농협")
        nameList.add("대구")
        nameList.add("시티")
        nameList.add("신한")
        nameList.add("우리")
        nameList.add("우체국")
        nameList.add("SC제일")
        nameList.add("제주")
        nameList.add("카카오")
        nameList.add("토스")
        nameList.add("하나")
    }

    private fun bottomSheetInit() {
        //persistent bottom sheet 제어
        behavior = BottomSheetBehavior.from(binding.modaleAccount.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.cover.isGone = true

        binding.bankSelect.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        listset()
        val adapter = OnboardingAccountAdapter(imgList, nameList)
        val manager = GridLayoutManager(this, 4)
        binding.modaleAccount.bankTable.layoutManager = manager
        binding.modaleAccount.bankTable.adapter = adapter.apply {
            setOnItemClickListener(object : OnboardingAccountAdapter.OnItemClickListener {
                override fun onItemClick(img: Drawable, name: String, position: Int) {
                    binding.bankName.text = name
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.cover.isGone = true
                    binding.bankName.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_text))
                    if (binding.accountEdit.text.isNotEmpty()) {
                        binding.nextbtn.isEnabled = true
                        binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                    }
                }
            })
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