package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityMypageEditdayBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MypageEditdayActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditdayBinding
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageEditdayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val firstday = binding.settleDay1
        val fifteenthday = binding.settleDay15
        val endday = binding.settleDayEnd
        val customday = binding.settleDayEdit
        val customtext = binding.settleDayEditText
        var message = ""
        var daytosend = 0

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(postSettleDay::class.java)

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.completedbtn.setOnClickListener {
            if (daytosend != 0) {
                var accessToken = ""
                runBlocking {
                    accessToken = userRepo.userAccessReadFlow.first().toString()
                }
                endpoint!!.postSettleDay("Bearer $accessToken", settledaytosend(daytosend.toString())).enqueue(object: Callback<Response<Void>> {
                    override fun onResponse(
                        call: Call<Response<Void>>,
                        response: Response<Response<Void>>
                    ) {
                        finish()
                    }

                    override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                        Toast.makeText(context, "연결 실패(마이페이지-정산일 수정)", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        val day = intent.getStringExtra("day")!!.toInt()

        if (day == 1) {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 1
        } else if (day == 15) {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 15
        } else if (day == 29 || day == 30 || day == 31) {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 29
        } else {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            binding.completedbtn.isEnabled = true
            binding.dayText.isGone = false
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            daytosend = day
            binding.settleDayEditText.text = Editable.Factory.getInstance().newEditable(day.toString())
        }

        firstday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 1
            hidekeyboard()
        }

        fifteenthday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 15
            hidekeyboard()
        }

        endday.setOnClickListener {
            firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
            customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.completedbtn.isEnabled = true
            binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
            binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
            daytosend = 29
            hidekeyboard()
        }

        customday.setOnClickListener {
            if (message.isNotEmpty()) {
                firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
                binding.completedbtn.isEnabled = true
                binding.dayText.isGone = false
                binding.settleDay1Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleDay15Text.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleTextEnd.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(this, R.color.graylight_text))
                binding.settleDayEditText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
                binding.dayText.setTextColor(ContextCompat.getColor(this, R.color.purpleblue_select))
                daytosend = message.toInt()
            } else {
                Toast.makeText(applicationContext, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            hidekeyboard()
        }

        customtext.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                }
                return false
            }
        })

        customtext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                message = customtext.text.toString()
                if (message.isNotEmpty()) {
                    if (message == "29" || message == "30" || message == "31") {
                        firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
                        customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        Toast.makeText(applicationContext, "설정하신 날짜가 29일, 30일, 31일 중 하나일 경우 자동으로 말일로 변경됩니다.", Toast.LENGTH_SHORT).show()
                        binding.completedbtn.isEnabled = true
                        binding.dayText.isGone = false
                        daytosend = 29
                        hidekeyboard()
                    } else if (message.toInt() > 31) {
                        Toast.makeText(applicationContext, "유효한 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        customtext.text.clear()
                    } else {
                        firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                        customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
                        binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                        binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purpleblue_select))
                        binding.completedbtn.isEnabled = true
                        binding.dayText.isGone = false
                        daytosend = message.toInt()
                    }
                } else {
                    firstday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                    fifteenthday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                    endday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                    customday.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                    binding.settleDay1Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleDay15Text.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleTextEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleTextEnddesc.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.settleDayEditText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.dayText.setTextColor(ContextCompat.getColor(applicationContext, R.color.graylight_text))
                    binding.completedbtn.isEnabled = false
                    binding.dayText.isGone = true
                    daytosend = 0
                }
            }
        })
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}