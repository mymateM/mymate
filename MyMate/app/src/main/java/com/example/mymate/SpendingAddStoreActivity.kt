package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivitySpendingaddstoreBinding

class SpendingAddStoreActivity: AppCompatActivity() {
    lateinit var binding: ActivitySpendingaddstoreBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpendingaddstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        var message = "없음"
        val store = intent.getStringExtra("store")
        var initialstore = intent.getStringExtra("store")
        val intent = Intent()

        if (!store.isNullOrEmpty()) {
            binding.storeEdit.text = Editable.Factory.getInstance().newEditable(store)
            val messageinit = (message.length - 1).toString() + "/15"
            binding.storeDigit.text = messageinit
            binding.storeDigit.setTextColor(ContextCompat.getColor(context, R.color.black_text))
        }

        binding.storeview.setOnClickListener {
            hidekeyboard()
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.storeEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                message = binding.storeEdit.text.toString()
                if (binding.storeEdit.text.isNotEmpty()) {
                    binding.storeDigit.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    val messagelength = (message.length).toString() + "/15"
                    binding.storeDigit.text = messagelength
                } else {
                    binding.storeDigit.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
                    binding.storeDigit.text = "0/15"
                    message = "없음"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.completedbtn.setOnClickListener {
            if (binding.storeEdit.text.isNotEmpty()) {
                intent.putExtra("store", binding.storeEdit.text.toString())
            }
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