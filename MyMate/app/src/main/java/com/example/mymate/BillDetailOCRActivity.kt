package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityBilldetailocrBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate

class BillDetailOCRActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilldetailocrBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private var year = 1
    private var month = 1
    private var today = 1

    var category = ""
    var savedPath = ""

    //TODO: 이미지 서버가 완성되면, savedPath를 삭제하는 대신 이미지를 서버로 전송할 것

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilldetailocrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        val toManager = Intent(context, BillManagerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        userRepo = DataStoreRepoUser(dataStore)

        category = intent.getStringExtra("category").toString()
        val day = intent.getStringExtra("itemday")
        val amount = intent.getStringExtra("itemamount")
        savedPath = intent.getStringExtra("savedUri").toString()

        if (!day.isNullOrBlank()) {
            year = day.substring(0 until 4).toInt()
            month = day.substring(4 until 6).toInt()
            today = day.substring(6 until 8).toInt()
        }

        bottomSheetInit()

        binding.ocrimg.setImageURI(savedPath?.toUri())

        binding.cover.isGone = true
        binding.closeoverlay.isGone = true
        binding.scanbtn.isGone = true
        binding.ocrimg.isGone = true
        binding.scandesc.isGone = true

        binding.completedbtn.isEnabled = true
        binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))

        binding.category.text = category
        val duedate = year.toString().substring(2 until 4) + "년 " + month.toString() + "월 " + today.toString() + "일"
        binding.duedate.text = duedate
        binding.amountEdit.text = Editable.Factory.getInstance().newEditable(amount)
        binding.showimage.setOnClickListener {
            binding.cover.isGone = false
            binding.closeoverlay.isGone = false
            binding.scandesc.isGone = false
            binding.scanbtn.isGone = false
            binding.ocrimg.isGone = false
        }
        binding.closeoverlay.setOnClickListener {
            binding.cover.isGone = true
            binding.closeoverlay.isGone = true
            binding.scanbtn.isGone = true
            binding.ocrimg.isGone = true
            binding.scandesc.isGone = true
        }

        binding.amountEdit.setOnEditorActionListener(object: TextView.OnEditorActionListener {
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

        binding.memoEdit.setOnClickListener {
            val toMemoEdit = Intent(this, BillAddMemoOCRActivity::class.java)
            if (binding.memo.text == "없음") {
                toMemoEdit.putExtra("memo", "")
            } else {
                toMemoEdit.putExtra("memo", binding.memo.text)
            }
            toMemoEdit.putExtra("category", category)
            toMemoEdit.putExtra("amount", binding.amountEdit.text.toString())
            toMemoEdit.putExtra("duedate", binding.duedate.text.toString())
            toMemoEdit.putExtra("savedUri", savedPath)
            startActivity(toMemoEdit)
        }

        binding.scanbtn.setOnClickListener {
            startActivity(Intent(this, BillCameraActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            if (File(savedPath!!.toUri().path!!).exists()) {
                File(savedPath.toUri().path!!).delete()
            }
        }
        binding.backbtn.setOnClickListener {
            startActivity(toManager)
            overridePendingTransition(android.R.anim.fade_in, R.anim.vertical_exit)
            if (File(savedPath!!.toUri().path!!).exists()) {
                File(savedPath.toUri().path!!).delete()
            }
        }

        val year = (LocalDate.now().year % 1000 % 100).toString()
        val month = LocalDate.now().monthValue.toString()
        val today = LocalDate.now().dayOfMonth.toString()
        val daytext = year + "년 " + month + "월 " + today + "일"
        binding.enrolldate.text = daytext

        billToSend()
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
            today = binding.datepicker.spinnerpicker.dayOfMonth
            year = binding.datepicker.spinnerpicker.year % 1000 % 100

            val duedate = year.toString() + "년 " + month.toString() + "월 " + today.toString() + "일"

            binding.duedate.text = duedate
            binding.duedate.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            binding.closeoverlay.isGone = true
            binding.scanbtn.isGone = true
            binding.ocrimg.isGone = true
            binding.scandesc.isGone = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val backintent = Intent(context, BillManagerActivity::class.java)
        backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(backintent)
        overridePendingTransition(android.R.anim.fade_in, R.anim.vertical_exit)
        if (File(savedPath!!.toUri().path!!).exists()) {
            File(savedPath.toUri().path!!).delete()
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

    private fun billToSend() {
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(postBill::class.java)
        var accessToken = ""
        var billtosend = billtosend()

        binding.completedbtn.setOnClickListener {
            runBlocking {
                accessToken = userRepo.userAccessReadFlow.first().toString()
            }
            billtosend.bill_memo = binding.memo.text.toString()
            if (year < 2000) {
                year = LocalDate.now().year
            }
            if (month > 12) {
                month = LocalDate.now().monthValue
            }
            if (today > 31) {
                today = LocalDate.now().dayOfMonth
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
            billtosend.bill_store = category
            billtosend.bill_payment_date = "$year-$month-$today"
            if (month < 10 && today < 10) {
                billtosend.bill_payment_date = "$year-0$month-0$today"
            } else if (month < 10) {
                billtosend.bill_payment_date = "$year-0$month-$today"
            } else if (today < 10) {
                billtosend.bill_payment_date = "$year-$month-0$today"
            }
            billtosend.bill_payment_amount = binding.amountEdit.text.toString()
            billtosend.bill_image = intent.getStringExtra("savedUri").toString()
            billtosend.virtual_accounts.add(virtualAccounts())
            var postResponse = postbillResponse()
            endpoint!!.postBill(Authorization = "Bearer $accessToken", req = billtosend).enqueue(object :
                Callback<postbillResponse> {
                override fun onResponse(
                    call: Call<postbillResponse>,
                    response: Response<postbillResponse>
                ) {
                    if (response.isSuccessful) {
                        postResponse = response.body()!!
                    }
                    val backintent = Intent(context, BillManagerActivity::class.java)
                    backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(backintent)
                    overridePendingTransition(android.R.anim.fade_in, R.anim.vertical_exit)
                    if (File(savedPath!!.toUri().path!!).exists()) {
                        File(savedPath.toUri().path!!).delete()
                    }
                }

                override fun onFailure(call: Call<postbillResponse>, t: Throwable) {
                    Toast.makeText(context, "연결 실패(고지서 추가)", Toast.LENGTH_SHORT).show()
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