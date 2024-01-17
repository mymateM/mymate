package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.mymate.databinding.ActivityMypageEditbudgetBinding

class MypageEditbudgetActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditbudgetBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageEditbudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.budget.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("budget")?.replace(",", ""))
        binding.budget.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT || p1 == EditorInfo.IME_ACTION_DONE || p1 == EditorInfo.IME_ACTION_SEARCH) {
                    hidekeyboard()
                }
                return false
            }
        })

        binding.back.setOnClickListener {
            finish()
        }

        binding.completedbtn.setOnClickListener {
            if (binding.budget.text.isNotEmpty() && binding.budget.text.toString().isDigitsOnly()) {
                if (binding.budget.text.toString().toInt() != 0) {
                    val intent = Intent()
                    intent.putExtra("budget", binding.budget.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(context, "0원은 예산으로 등록할 수 없어요!", Toast.LENGTH_SHORT).show()
                }
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