package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityBilladdBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class BillAddActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilladdBinding
    lateinit var context: Context
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var userRepo: DataStoreRepoUser

    private var year = 1
    private var month = 1
    private var day = 1

    var billtosend = billtosend()

    var memo: String? = "없음"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var category = intent.getStringExtra("category")
        binding = ActivityBilladdBinding.inflate(layoutInflater)
        context = this
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)

        binding.cover.isGone = true
        bottomSheetInit()
        binding.category.text = category
        binding.completedbtn.isEnabled = false
        binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))

        binding.amountEdit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                }
                return false
            }
        })

        binding.amountEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.amountEdit.text.toString().isNotEmpty() && binding.amountEdit.text.toString().toInt() != 0) {
                    binding.completedbtn.isEnabled = true
                    binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                } else {
                    binding.completedbtn.isEnabled = false
                    binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        if (memo == "없음") {
            binding.memo.text = "없음"
            binding.memo.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
        } else {
            binding.memo.text = memo
            binding.memo.setTextColor(ContextCompat.getColor(context, R.color.black_text))
        }

        binding.memoEdit.setOnClickListener {
            val intent = Intent(context, BillAddMemoActivity::class.java)
            if (memo == "없음") {
                intent.putExtra("memo", "")
            } else {
                intent.putExtra("memo", memo)
            }
            startActivityForResult(intent, 97)
        }

        val thisyear = (LocalDate.now().year % 1000 % 100).toString()
        val thismonth = LocalDate.now().monthValue.toString()
        val today = LocalDate.now().dayOfMonth.toString()
        val daytext = thisyear + "년 " + thismonth + "월 " + today + "일"
        binding.enrolldate.text = daytext

        binding.bottomlayout.setOnClickListener {
            hidekeyboard()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }

        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(postBill::class.java)
        var accessToken = ""

        binding.completedbtn.setOnClickListener {
            runBlocking {
                accessToken = userRepo.userAccessReadFlow.first().toString()
            }
            billtosend.bill_memo = binding.memo.text.toString()
            Log.d("yearTest", year.toString())
            if (binding.duedate.text == "없음") {
                year = LocalDate.now().year
                month = LocalDate.now().monthValue
                day = LocalDate.now().dayOfMonth
                billtosend.bill_payment_date = "$year-$month-$day"
                if (month < 10 && day < 10) {
                    billtosend.bill_payment_date = "$year-0$month-0$day"
                } else if (month < 10) {
                    billtosend.bill_payment_date = "$year-0$month-$day"
                } else if (day < 10) {
                    billtosend.bill_payment_date = "$year-$month-0$day"
                }
            } else {
                year = binding.duedate.text.substring(0 until binding.duedate.text.indexOf("년")).toInt()
                month = binding.duedate.text.substring(binding.duedate.text.indexOf("년") + 2 until binding.duedate.text.indexOf("월")).toInt()
                day = binding.duedate.text.substring(binding.duedate.text.indexOf("월") + 2 until binding.duedate.text.indexOf("일")).toInt()
                billtosend.bill_payment_date = "20$year-$month-$day"
                if (month < 10 && day < 10) {
                    billtosend.bill_payment_date = "20$year-0$month-0$day"
                } else if (month < 10) {
                    billtosend.bill_payment_date = "20$year-0$month-$day"
                } else if (day < 10) {
                    billtosend.bill_payment_date = "20$year-$month-0$day"
                }
            }
            if (category == "도시가스") {
                billtosend.bill_category_title = "GAS"
            } else if (category == "전기") {
                billtosend.bill_category_title = "ELECTRICITY"
            } else if (category == "수도") {
                billtosend.bill_category_title = "WATER"
            } else {
                billtosend.bill_category_title = "ETC"
            }
            billtosend.bill_store = category!!
            if (binding.amountEdit.text.isNotEmpty()) {
                billtosend.bill_payment_amount = binding.amountEdit.text.toString()
            } else {
                billtosend.bill_payment_amount = "0"
            }
            billtosend.bill_image = ""
            billtosend.virtual_accounts.add(virtualAccounts())
            var postResponse = postbillResponse()
            endpoint!!.postBill(Authorization = "Bearer $accessToken", req = billtosend).enqueue(object : Callback<postbillResponse> {
                override fun onResponse(
                    call: Call<postbillResponse>,
                    response: Response<postbillResponse>
                ) {
                    if (response.isSuccessful) {
                        postResponse = response.body()!!
                    }
                    finish()
                }

                override fun onFailure(call: Call<postbillResponse>, t: Throwable) {
                    Toast.makeText(context, "연결 실패(고지서 추가)", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.datepicker.root)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.dateEdit.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.datepicker.confirmbtn.setOnClickListener {
            month = (binding.datepicker.spinnerpicker.month + 1)
            day = binding.datepicker.spinnerpicker.dayOfMonth
            year = binding.datepicker.spinnerpicker.year % 1000 % 100

            val duedate = year.toString() + "년 " + month.toString() + "월 " + day.toString() + "일"
            binding.duedate.text = duedate
            binding.duedate.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }
    }

    override fun onResume() {
        super.onResume()

        if (memo == "없음") {
            binding.memo.text = "없음"
            binding.memo.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
        } else {
            binding.memo.text = memo
            binding.memo.setTextColor(ContextCompat.getColor(context, R.color.black_text))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 97) {
                memo = data?.getStringExtra("memo")
                if (!memo.isNullOrBlank()) {
                    binding.memo.text = memo
                    binding.memo.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                } else {
                    binding.memo.text = "없음"
                    binding.memo.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                }
            }
        }
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}