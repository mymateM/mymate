package com.example.mymate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.databinding.ActivityAlarmBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding

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
        pager2Adapter.addFragment(AlarmActFragment())
        pager2Adapter.addFragment(AlarmSpdFragment())

        binding.alarmpager.offscreenPageLimit = 2
        binding.alarmpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
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