package com.example.mymate.presentation.settlement

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.*
import com.example.mymate.databinding.ActivitySettlementReportBinding
import com.example.mymate.util.ViewPager2Adapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

class SettlementReportActivity: AppCompatActivity() {
    private var _binding: ActivitySettlementReportBinding? = null
    private val binding get() = _binding!!
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context
    private val householdFragment = SettlementReportHouseholdFragment()
    private val meFragment = SettlementReportMeFragment()
    private val viewModel: SettlementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettlementReportBinding.inflate(layoutInflater)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        initViewPager()
        viewModel.initReport()

        viewModel.reportPeriodText.observe(this) {
            binding.thisperiod.text = it
        }

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)

    }

    private fun initViewPager() {
        val pager2Adapter = ViewPager2Adapter(this)
        pager2Adapter.addFragment(householdFragment)
        pager2Adapter.addFragment(meFragment)

        binding.reportPager.offscreenPageLimit = 2
        binding.reportPager.isUserInputEnabled = false

        binding.reportPager.apply {
            adapter = pager2Adapter
        }

        TabLayoutMediator(binding.reportTab, binding.reportPager) {tab, position ->
            when(position) {
                0 -> tab.text = "전체 예산 및 지출"
                1 -> tab.text = "개인 지출"
            }
        }.attach()
    }
}