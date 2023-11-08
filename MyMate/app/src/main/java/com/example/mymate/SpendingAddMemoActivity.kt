package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivityBilladdmemoBinding

class SpendingAddMemoActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilladdmemoBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilladdmemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        var message = "없음"
        val memo = intent.getStringExtra("memo")
        var initialmessage = "없음"
        val intent = Intent()

        if (!memo.isNullOrEmpty()) {
            binding.memoEdit.text = Editable.Factory.getInstance().newEditable(memo)
            if (memo != null) {
                initialmessage = memo
            }
            val messageinit = (message.length - 1).toString() + "/15"
            binding.memoDigit.text = messageinit
            binding.memoDigit.setTextColor(ContextCompat.getColor(context, R.color.black_text))
        }

        binding.memoview.setOnClickListener {
            hidekeyboard()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.memoEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = binding.memoEdit.text.toString()
                if (binding.memoEdit.text.isNotEmpty()) {
                    binding.memoDigit.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    val messagelength = (message.length).toString() + "/15"
                    binding.memoDigit.text = messagelength
                } else {
                    binding.memoDigit.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
                    binding.memoDigit.text = "0/15"
                    message = "없음"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.completedbtn.setOnClickListener {
            if (binding.memoEdit.text.isNotEmpty()) {
                message = binding.memoEdit.text.toString()
            } else {
                message = "없음"
            }
            intent.putExtra("memo", message)
            setResult(RESULT_OK, intent)
            finish()
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