package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

        bottomSheetInit()

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.contbtn.setOnClickListener {
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
        }

        binding.nextbtn.setOnClickListener {
            //TODO: 여기서 정보 저장
            startActivity(Intent(this, OnboardingInviteActivity::class.java))
        }

        binding.accountview.setOnClickListener {
            hidekeyboard()
        }

        binding.accountEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.accountEdit.text.isNotEmpty() && !binding.bankName.text.isNullOrBlank()) {
                    binding.nextbtn.isEnabled = true
                    binding.nextbtn.setBackgroundResource(R.drawable.button_wfseleted)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        //TODO: 정보형 확정나면 adapter 작성
    }

    private fun listset() {
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_busan_gyungnam)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_kb)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_ibk)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_nh)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_daegu)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_citi)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.edit)!!)
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
        nameList.add("새마을")
        nameList.add("신한")
        nameList.add("우리")
        nameList.add("우체국")
        nameList.add("SC제일")
        nameList.add("제주")
        nameList.add("카카오뱅크")
        nameList.add("토스뱅크")
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
                    if (position == 12 || position == 13) {
                        binding.bankName.text = name
                    } else {
                        binding.bankName.text = "${name}은행"
                    }
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.cover.isGone = true
                    binding.bankName.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_text))
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