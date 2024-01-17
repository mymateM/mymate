package com.example.mymate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mymate.databinding.ActivitySettlementBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SettlementActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettlementBinding
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context

    val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettlementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this
        binding.settlementdismiss.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.desc.isGone = true
        binding.descheader.isGone = true
        binding.toppopup.isGone = true

        binding.about.setOnClickListener {
            if (binding.desc.isGone) {
                binding.desc.isGone = false
                binding.descheader.isGone = false
            } else {
                binding.desc.isGone = true
                binding.descheader.isGone = true
            }
        }

        binding.scrollview.setOnClickListener {
            binding.desc.isGone = true
            binding.descheader.isGone = true
        }

        binding.nestedview.setOnClickListener {
            binding.desc.isGone = true
            binding.descheader.isGone = true
        }

        binding.toReport.setOnClickListener {
            startActivity(Intent(context, SettlementReportActivity::class.java))
            overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        bottomSheetInit()
        dataInit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }

    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.modale.root)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.cover.isGone = true

        binding.modalepop.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        binding.modale.modaledismiss.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        binding.modale.root.setOnClickListener {

        }
    }

    private fun dataInit() {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        val dateEndpoint = retrofit?.create(getSettlementDate::class.java)
        val myEndpoint = retrofit?.create(getMySettleInfo::class.java)
        val mateEndpoint = retrofit?.create(getMateSettleInfo::class.java)

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)

        binding.modale.copyandsendbtn.isEnabled = false

        dateEndpoint!!.getSettlementDate("Bearer $accessToken").enqueue(object : Callback<settlementDateResponse> {
            override fun onResponse(
                call: Call<settlementDateResponse>,
                response: Response<settlementDateResponse>
            ) {
                if (response.isSuccessful) {
                    var date = LocalDate.now()
                    var previousdate = date
                    if (date.dayOfMonth < response.body()!!.data.toInt()) {
                        //date = date.minusMonths(1).withDayOfMonth(response.body()!!.data.toInt())
                    } else {
                        //date = date.withDayOfMonth(response.body()!!.data.toInt())
                    }
                    date = date.withDayOfMonth(response.body()!!.data.toInt())
                    previousdate = date.minusMonths(1).withDayOfMonth(response.body()!!.data.toInt() + 1)

                    val startDate = previousdate.format(formatter)
                    val endDate = date.format(formatter)
                    val periodtxt = "${previousdate.monthValue}.${previousdate.dayOfMonth} - ${date.monthValue}.${date.dayOfMonth}"
                    binding.settlementmonth.text = periodtxt

                    myEndpoint!!.getMySettleInfo("Bearer $accessToken", startDate, endDate).enqueue(object : Callback<mySettleInfoResponse> {
                        @RequiresApi(Build.VERSION_CODES.P)
                        override fun onResponse(
                            call: Call<mySettleInfoResponse>,
                            response: Response<mySettleInfoResponse>
                        ) {
                            if (response.isSuccessful) {
                                val mydata = response.body()!!.data
                                val total = SpannableStringBuilder("${digitprocessing(mydata.household_expense_total)}원")
                                total.setSpan(TypefaceSpan(suitBoldTypeface), total.length - 1, total.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.totalamount.text = total
                                val movetotal = SpannableStringBuilder("${digitprocessing(mydata.user.settlement_amount)}원")
                                movetotal.setSpan(TypefaceSpan(suitBoldTypeface), movetotal.length - 1, movetotal.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.takeorgiveamount.text = movetotal

                                if (mydata.user.is_settlement_sender) {
                                    binding.takeorgive.text = "정산을 해야 해요"
                                    val modaletxt = SpannableStringBuilder("${digitprocessing(mydata.user.settlement_amount)}원 보내러 가기")
                                    modaletxt.setSpan(TypefaceSpan(montBoldTypeface), 0, digitprocessing(mydata.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    binding.modaletext.text = modaletxt
                                    binding.modale.copyandsendtxt.text = "계좌 복사"
                                    binding.limegraphtop.isGone = false
                                } else {
                                    binding.takeorgive.text = "정산을 받아야 해요"
                                    val modaletxt = SpannableStringBuilder("${digitprocessing(mydata.user.settlement_amount)}원 받으러 가기")
                                    modaletxt.setSpan(TypefaceSpan(montBoldTypeface), 0, digitprocessing(mydata.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    binding.modaletext.text = modaletxt
                                    binding.modale.copyandsendtxt.text = "송금 요청"
                                    binding.limegraphtop.isGone = true
                                }

                                val myportion = mydata.user.ratio_expense.toFloat()
                                val myreal = mydata.user.real_expense.toFloat()
                                val housetotal = mydata.household_expense_total.toFloat()

                                binding.myportionguide.setGuidelinePercent(0.05f + ((myportion / housetotal)) * 0.90f)
                                binding.realportionguide.setGuidelinePercent(0.05f + ((myreal / housetotal)) * 0.90f)
                            }
                        }

                        override fun onFailure(call: Call<mySettleInfoResponse>, t: Throwable) {
                            Toast.makeText(context, "연결 실패(정산-나)", Toast.LENGTH_SHORT).show()
                        }

                    })

                    mateEndpoint!!.getMateSettleInfo("Bearer $accessToken", startDate, endDate).enqueue(object : Callback<mateSettleInfoResponse> {
                        @RequiresApi(Build.VERSION_CODES.P)
                        override fun onResponse(
                            call: Call<mateSettleInfoResponse>,
                            response: Response<mateSettleInfoResponse>
                        ) {
                            if (response.isSuccessful) {
                                val matedata = response.body()!!.data
                                val modale = binding.modale
                                val adapter = SettlementAdapter(matedata.roommates)
                                val manager = LinearLayoutManager(context)

                                var accountType = ""
                                var accountNumber = ""
                                var mateName = ""

                                var mateId = ""

                                if (matedata.user.is_settlement_sender) {
                                    val header = SpannableStringBuilder("내가 이번 달에 보낼 돈은\n총 ${digitprocessing(matedata.user.settlement_amount)}원 입니다!")
                                    header.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 8, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    header.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 17, 17 + digitprocessing(matedata.user.settlement_amount).length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    header.setSpan(TypefaceSpan(montBoldTypeface), 17, 17 + digitprocessing(matedata.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    modale.modaleheader.text = header
                                    binding.modale.copyandsendbtn.setOnClickListener {
                                        when (accountType) {
                                            "KB" -> accountType = "국민"
                                            "SC" -> accountType = "SC제일"
                                            "WOORI" -> accountType = "우리"
                                        }

                                        val account = "$accountType $accountNumber"
                                        val clip = ClipData.newPlainText("account", account)
                                        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(clip)

                                        val toasttxt = "${mateName}의\n계좌를 복사했어요"
                                        binding.toastTxt.text= toasttxt
                                        binding.toppopup.isGone = false
                                        binding.toHome.setOnClickListener {
                                            startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                        }
                                        binding.toppopup.setOnClickListener {

                                        }
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            binding.toppopup.isGone = true
                                        }, 3000)
                                    }
                                } else {
                                    val header = SpannableStringBuilder("내가 이번 달에 받을 돈은\n총 ${digitprocessing(matedata.user.settlement_amount)}원 입니다!")
                                    header.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 8, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    header.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 17, 17 + digitprocessing(matedata.user.settlement_amount).length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    header.setSpan(TypefaceSpan(montBoldTypeface), 17, 17 + digitprocessing(matedata.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                    modale.modaleheader.text = header
                                    binding.modale.copyandsendbtn.setOnClickListener {

                                        val requestEndpoint = retrofit?.create(sendMoneyRequest::class.java)
                                        if (mateId != "") {
                                            requestEndpoint!!.sendMoneyRequest("Bearer $accessToken", mateId).enqueue(object : Callback<Response<Void>> {
                                                override fun onResponse(
                                                    call: Call<Response<Void>>,
                                                    response: Response<Response<Void>>
                                                ) {
                                                    val toasttxt = "${mateName}에게\n송금 요청을 보냈어요"
                                                    binding.toastTxt.text= toasttxt
                                                    binding.toppopup.isGone = false
                                                    binding.toHome.setOnClickListener {
                                                        startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                                    }
                                                    binding.toppopup.setOnClickListener {

                                                    }
                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        binding.toppopup.isGone = true
                                                    }, 3000)
                                                }

                                                override fun onFailure(
                                                    call: Call<Response<Void>>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(context, "연결 실패(송금 요청)", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                    }
                                }

                                modale.modallist.layoutManager = manager
                                modale.modallist.adapter = adapter.apply {
                                    setOnItemClickListener(object : SettlementAdapter.OnItemClickListener {
                                        override fun onItemClick(
                                            item: mateSettleInfo,
                                            position: Int
                                        ) {
                                            if (accountType == matedata.roommates[position].account_bank && accountNumber == matedata.roommates[position].account_number) {
                                                modale.copyandsendbtn.isEnabled = false
                                                modale.copyandsendbtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_loginbardefault))
                                                accountNumber = ""
                                                accountType = ""
                                                mateName = ""
                                                mateId = ""
                                            } else {
                                                accountType = matedata.roommates[position].account_bank
                                                accountNumber = matedata.roommates[position].account_number
                                                modale.copyandsendbtn.isEnabled = accountType != ""
                                                mateName = matedata.roommates[position].name
                                                modale.copyandsendbtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_loginbarselected))
                                                mateId = matedata.roommates[position].id
                                            }
                                        }
                                    })
                                }
                            }
                        }

                        override fun onFailure(call: Call<mateSettleInfoResponse>, t: Throwable) {
                            Toast.makeText(context, "연결 실패(정산-가구)", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<settlementDateResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(정산-정산일)", Toast.LENGTH_SHORT).show()
            }

        })
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
}