package com.example.mymate.presentation.settlement

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.example.mymate.*
import com.example.mymate.databinding.ActivitySettlementBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

class SettlementActivity : AppCompatActivity() {
    private var _binding: ActivitySettlementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettlementViewModel by viewModels()
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context

    val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initVisibility()
        initBottomSheet()
        initData()
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        binding.settlementdismiss.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.about.setOnClickListener {
            binding.descheader.isGone = !binding.desc.isGone
            binding.desc.isGone = !binding.desc.isGone
        }

        binding.scrollview.setOnClickListener {
            initVisibility()
        }

        binding.nestedview.setOnClickListener {
            initVisibility()
        }

        binding.toReport.setOnClickListener {
            startActivity(Intent(context, SettlementReportActivity::class.java))
            overridePendingTransition(R.anim.right_enter, R.anim.none)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }

    private fun initBinding() {
        _binding = ActivitySettlementBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    private fun initVisibility() {
        binding.desc.isGone = true
        binding.descheader.isGone = true
    }

    private fun initBottomSheet() {
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
            //touch event 막기 위한 empty body
        }
    }

    private fun initData() {
        binding.modale.copyandsendbtn.isEnabled = false //현재 data 없으므로 임시적 disabled

        viewModel.totalPortionGuide.observe(this) {
            binding.myportionguide.setGuidelinePercent(it)
        }

        viewModel.myPortionGuide.observe(this) {
            binding.realportionguide.setGuidelinePercent(it)
        }

        /* dateEndpoint!!.getSettlementDate("Bearer $accessToken").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    var date = LocalDate.now()
                    var previousdate = date
                    if (date.dayOfMonth < response.body()!!.data!!.toInt()) {
                        //date = date.minusMonths(1).withDayOfMonth(response.body()!!.data.toInt())
                    } else {
                        //date = date.withDayOfMonth(response.body()!!.data.toInt())
                    }
                    date = date.withDayOfMonth(response.body()!!.data!!.toInt())
                    previousdate = date.minusMonths(1).withDayOfMonth(response.body()!!.data!!.toInt() + 1)

                    val startDate = previousdate.format(formatter)
                    val endDate = date.format(formatter)
                    val periodtxt = "${previousdate.monthValue}.${previousdate.dayOfMonth} - ${date.monthValue}.${date.dayOfMonth}"
                    binding.settlementmonth.text = periodtxt

                    //여기서부터 모달

                    mateEndpoint!!.getMateSettleInfo("Bearer $accessToken", startDate, endDate).enqueue(object : Callback<MemberSettlementInfoResponse> {
                        @RequiresApi(Build.VERSION_CODES.P)
                        override fun onResponse(
                            call: Call<MemberSettlementInfoResponse>,
                            response: Response<MemberSettlementInfoResponse>
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
                                            item: MemberMonthlyStatus,
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

                        override fun onFailure(call: Call<MemberSettlementInfoResponse>, t: Throwable) {
                            Toast.makeText(context, "연결 실패(정산-가구)", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(정산-정산일)", Toast.LENGTH_SHORT).show()
            }

        }) */
    }
}