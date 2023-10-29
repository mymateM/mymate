package com.example.mymate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.databinding.ActivityAlarmBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding
    private var act = false
    private var spd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alarmdissmiss.setOnClickListener{
            finish()
        }

        initViewPager()
    }

    private fun initViewPager() {
        var pager2Adapter = viewPager2Adapter(this)
        val actFragment = AlarmActFragment()
        val spdFragment = AlarmSpdFragment()
        pager2Adapter.addFragment(actFragment)
        pager2Adapter.addFragment(spdFragment)

        binding.alarmpager.offscreenPageLimit = 2
        binding.alarmpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    /*if (position == 0) {
                        binding.tabBadge0.isGone = true
                    } else if (position == 1) {
                        binding.tabBadge1.isGone = true
                    }*/
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