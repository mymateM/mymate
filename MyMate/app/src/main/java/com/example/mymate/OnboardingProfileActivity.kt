package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
        val imgList = arrayListOf<ImageView>()
        imgList.add(bottomsheet.gridimg1)
        imgList.add(bottomsheet.gridimg2)
        imgList.add(bottomsheet.gridimg3)
        imgList.add(bottomsheet.gridimg4)
        imgList.add(bottomsheet.gridimg5)
        imgList.add(bottomsheet.gridimg6)
        imgList.add(bottomsheet.gridimg7)
        imgList.add(bottomsheet.gridimg8)

        bottomSheetInit()

        bottomsheet.gridimg1.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain1.drawable)
            imgSelection(bottomsheet.gridimg1, imgList)
        }

        bottomsheet.gridimg2.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain2.drawable)
            imgSelection(bottomsheet.gridimg2, imgList)
        }

        bottomsheet.gridimg3.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain3.drawable)
            imgSelection(bottomsheet.gridimg3, imgList)
        }

        bottomsheet.gridimg4.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain4.drawable)
            imgSelection(bottomsheet.gridimg4, imgList)
        }

        bottomsheet.gridimg5.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain5.drawable)
            imgSelection(bottomsheet.gridimg5, imgList)
        }

        bottomsheet.gridimg6.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain6.drawable)
            imgSelection(bottomsheet.gridimg6, imgList)
        }

        bottomsheet.gridimg7.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain7.drawable)
            imgSelection(bottomsheet.gridimg7, imgList)
        }

        bottomsheet.gridimg8.setOnClickListener {
            profileimg.setImageDrawable(bottomsheet.gridmain8.drawable)
            imgSelection(bottomsheet.gridimg8, imgList)
        }

        tonext.setOnClickListener {
            //TODO: 여기서 값을 초기화하거나......
            startActivity(Intent(this, OnboardingAccountActivity::class.java))
        }

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = nameEdit.text.toString()
                putDatabutton.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        putDatabutton.setOnClickListener {
            //TODO: 여기서 값을 서버로 올리거나 클래스에 저장함
            startActivity(Intent(this, OnboardingAccountActivity::class.java))
        }
    }

    private fun imgSelection(profilepic: ImageView, picList: ArrayList<ImageView>) {
        for (i in 0 until picList.size) {
            if (profilepic == picList[i]) {
                profilepic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.button_selectedprofile))
            } else {
                profilepic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.box_radius20))
            }
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