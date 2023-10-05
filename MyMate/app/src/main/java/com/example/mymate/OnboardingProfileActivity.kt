package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isGone
import com.airbnb.lottie.utils.Utils
import com.example.mymate.databinding.ActivityOnboardingProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class OnboardingProfileActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingProfileBinding
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tonext = binding.contbtn
        val profileimg = binding.profileset
        val bottomsheet = binding.profilepersistent
        val nameEdit = binding.nameEdit
        val putDatabutton = binding.nextbtn
        var message = ""

        bottomSheetInit()

        bottomsheet.gridimg1.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg1.drawable)
        }

        bottomsheet.gridimg2.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg2.drawable)
        }

        bottomsheet.gridimg3.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg3.drawable)
        }

        bottomsheet.gridimg4.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg4.drawable)
        }

        bottomsheet.gridimg5.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg5.drawable)
        }

        bottomsheet.gridimg6.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg6.drawable)
        }

        bottomsheet.gridimg7.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg7.drawable)
        }

        bottomsheet.gridimg8.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridimg8.drawable)
        }

        tonext.setOnClickListener {
            //TODO: 여기서 값을 초기화하거나......
            startActivity(Intent(this, OnboardingAccountActivity::class.java))
        }

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = nameEdit.text.toString()
                if (message.isNotEmpty()) {
                    putDatabutton.isEnabled = true
                    //TODO: 버튼 이미지 바꾸기
                } else {
                    putDatabutton.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        putDatabutton.setOnClickListener {
            //TODO: 여기서 값을 서버로 올리거나 클래스에 저장함
            startActivity(Intent(this, OnboardingAccountActivity::class.java))
        }
    }

    private fun bottomSheetInit() {
        //persistent bottom sheet 제어
        behavior = BottomSheetBehavior.from(binding.profilepersistent.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true

        binding.profileroot.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                binding.profileEdit.isGone = false
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            hidekeyboard()
        }

        binding.profileset.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                binding.profileEdit.isGone = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                binding.profileEdit.isGone = false
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            hidekeyboard()
        }

        binding.profileEdit.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.profileEdit.isGone = true
            hidekeyboard()
        }

        binding.profilepersistent.modaledismiss.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.profileEdit.isGone = false
            hidekeyboard()
        }

        binding.profilepersistent.selectBtn.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.profileEdit.isGone = false
            hidekeyboard()
        }

        val nameedit = binding.nameEdit
        nameedit.setOnFocusChangeListener { view, b ->
            if (b == true) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.profileEdit.isGone = false
            }
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