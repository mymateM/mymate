package com.example.mymate.presentation.settlement

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.*
import com.example.mymate.data.dto.report.MemberMonthlyStatus
import com.example.mymate.databinding.ActivitySettlementBinding
import com.example.mymate.presentation.main.MainActivity
import com.example.mymate.presentation.settlement.adapter.SettlementAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.format.DateTimeFormatter

class SettlementActivity : AppCompatActivity() {
    private var _binding: ActivitySettlementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettlementViewModel by viewModels()
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var context: Context

    val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initVisibility()
        initBottomSheetBehavior()
        initData()
        initModale()
        context = this
        binding.toppopup.isGone = true

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

    private fun initBottomSheetBehavior() {
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
    }
        /*
                    //여기서부터 모달
                                var accountType = ""
                                var accountNumber = ""
                                var mateName = ""

                                var mateId = ""

                                if (matedata.user.is_settlement_sender) {
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
                                    } */

    private fun initModale() {
        binding.modale.modallist.layoutManager = LinearLayoutManager(this)
        var accountType = ""
        var accountNumber = ""
        var housemateName = ""
        var mateId = ""
        var presentPosition = -1
        viewModel.isSender.observe(this) {
            binding.modale.modallist.adapter = SettlementAdapter(viewModel.roomMates.value ?: ArrayList()) //TODO: 현재는 roomMates 정보가 안 변한다고 가정하고 있다. 새로고침 도입 등 필요
                .apply {
                    setOnItemClickListener(object : SettlementAdapter.OnItemClickListener {
                        override fun onItemClick(item: MemberMonthlyStatus, position: Int) {
                            if (presentPosition == position) {
                                binding.modale.copyandsendbtn.run {
                                    isEnabled = false
                                    setImageDrawable(ContextCompat.getDrawable(this@SettlementActivity, R.drawable.button_loginbardefault))
                                    accountNumber = ""
                                    accountType = ""
                                    mateId = ""
                                    housemateName = ""
                                }
                            } else {
                                binding.modale.copyandsendbtn.run {
                                    accountType = viewModel.roomMates.value?.get(position)?.account_bank ?: ""
                                    accountNumber = viewModel.roomMates.value?.get(position)?.account_number ?: ""
                                    housemateName = viewModel.roomMates.value?.get(position)?.name ?: ""
                                    mateId = viewModel.roomMates.value?.get(position)?.id ?: ""
                                    isEnabled = mateId != ""
                                    if (accountType != "") binding.modale.copyandsendbtn.setImageDrawable(ContextCompat.getDrawable(this@SettlementActivity, R.drawable.button_loginbarselected))
                                }
                            }
                        }

                    })
                }
            if (it) {
                binding.modale.receivenotiheavy.text = "동거인 별 보낼 돈"
                binding.modale.copyandsendbtn.setOnClickListener {
                    when (accountType) { //TODO: 대응 가능한 은행 목록이 도출되면, Enum 등으로 만들어 뺄 것.
                        "KB" -> accountType = "국민"
                        "SC" -> accountType = "SC제일"
                        "WOORI" -> accountType = "우리"
                    }

                    val account = "$accountType $accountNumber"
                    val clip = ClipData.newPlainText("account", account)
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(clip)

                    val popupText = "$housemateName${viewModel.popupText.value}"
                    binding.toastTxt.text = popupText
                    binding.toppopup.isGone = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.toppopup.isGone = true
                    }, 3000)
                }
            }
            else {
                binding.modale.receivenotiheavy.text = "동거인 별 받을 돈"
                binding.modale.copyandsendbtn.setOnClickListener {
                    if (mateId != "") {
                        val popupText = "$housemateName${viewModel.popupText.value}"
                        binding.toastTxt.text = popupText
                        binding.toppopup.isGone = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.toppopup.isGone = true
                        }, 3000)
                    }
                }
            }
        }

        viewModel.modaleHeaderText.observe(this) {
            binding.modale.modaleheader.text = it
        }

        binding.toHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }
}