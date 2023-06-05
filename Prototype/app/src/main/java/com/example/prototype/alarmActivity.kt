package com.example.prototype

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.prototype.databinding.ActivityAlarmBinding
import com.example.prototype.ui.theme.PrototypeTheme
import com.google.android.material.tabs.TabLayoutMediator

class AlarmActivity : AppCompatActivity() {

    lateinit var binding : ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alarmback.setOnClickListener{
            finish()
        }

        initViewPager()

    }

    private fun initViewPager() {
        var viewPager2Adapter = ViewPager2Adapter(this)
        viewPager2Adapter.addFragment(alarm_activity())
        viewPager2Adapter.addFragment(alarm_spending())

        binding.alarmpager.offscreenPageLimit = 2
        binding.alarmpager.apply {
            adapter = viewPager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(binding.toptab, binding.alarmpager) { tab, position ->
            Log.e("YMC", "ViewPagerPosition : $position")
            when (position) {
                0 -> tab.text = "활동 알림"
                1 -> tab.text = "지출 알림"
            }
        }.attach()
    }

}