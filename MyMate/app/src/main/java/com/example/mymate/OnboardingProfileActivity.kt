package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
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

        putDatabutton.isEnabled = false
        val postpond = SpannableStringBuilder("나중에 설정할게요")
        postpond.setSpan(UnderlineSpan(), 0, postpond.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.contbtn.text = postpond

        //전시용
        binding.backbtn.isEnabled = false

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
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = nameEdit.text.toString()
                putDatabutton.isEnabled = message.isNotEmpty()
                if (putDatabutton.isEnabled) {
                    putDatabutton.setBackgroundResource(R.drawable.button_wfseleted)
                } else {
                    putDatabutton.setBackgroundResource(R.drawable.button_wfdefault)
                }
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        nameEdit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    nameEdit.text = Editable.Factory.getInstance().newEditable(message.replace("\n", ""))
                    hidekeyboard()
                }
                return false
            }

        })

        nameEdit.onFocusChangeListener = OnFocusChangeListener { p0, p1 ->
            if (p1) {
                nameEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purpleblue_select)
            } else {
                nameEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.graylight_basic)
            }
        }

        putDatabutton.setOnClickListener {
            //TODO: 여기서 값을 서버로 올리거나 클래스에 저장함
            startActivity(Intent(this, OnboardingAccountActivity::class.java))
            overridePendingTransition(R.anim.none, R.anim.none)
        }

        binding.backbtn.setOnClickListener {
            //finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun imgSelection(profilepic: ImageView, picList: ArrayList<ImageView>) {
        for (i in 0 until picList.size) {
            if (profilepic == picList[i]) {
                profilepic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.button_selectedprofile))
            } else {
                picList[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.box_radius20))
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