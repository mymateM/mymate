package com.example.mymate.presentation.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.*
import com.example.mymate.data.dto.common.DefaultResponse
import com.example.mymate.data.dto.notification.UserExpNotiDetail
import com.example.mymate.data.dto.notification.response.UserExpNotiResponse
import com.example.mymate.databinding.ActivityAlarmBinding
import com.example.mymate.presentation.home.MainHomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {
    private var act = false
    private var spd = false

    private var _binding: ActivityAlarmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        binding.alarmdissmiss.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        viewModel.isBadgeGone.observe(this) {
            binding.tabBadge.isGone = it
        }

        initViewPager()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }

    private fun initBinding() {
        _binding = ActivityAlarmBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        setContentView(binding.root)
    }

    private fun initViewPager() {
        var pager2Adapter = viewPager2Adapter(this)
        val actFragment = AlarmActFragment()
        val spdFragment = AlarmSpdFragment()
        pager2Adapter.addFragment(actFragment)
        pager2Adapter.addFragment(spdFragment)

        viewModel.checkExpNoti()

        binding.alarmpager.offscreenPageLimit = 2
        binding.alarmpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        viewModel.getActNoti()
                    }
                    if (position == 1) {
                        viewModel.getExpNoti()
                        viewModel.readExpNoti()
                        viewModel.checkExpNoti()
                    }
                }
            })
        }

        TabLayoutMediator(binding.alarmtab, binding.alarmpager) { tab, position ->
            when (position) {
                0 -> tab.text = "활동 알림"
                1 -> tab.text = "지출 알림"
            }
        }.attach()
    }
}