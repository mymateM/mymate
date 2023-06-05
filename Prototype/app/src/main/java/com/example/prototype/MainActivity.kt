package com.example.prototype

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.prototype.databinding.ActivityMainBinding
import com.example.prototype.ui.theme.PrototypeTheme
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
    }

    private fun initViewPager() {
        var viewPager2Adapter = ViewPager2Adapter(this)
        viewPager2Adapter.addFragment(home_main())
        viewPager2Adapter.addFragment(spending_home())
        viewPager2Adapter.addFragment(report_home())
        viewPager2Adapter.addFragment(mypage_home())

        binding.pager.offscreenPageLimit = 4
        binding.pager.setUserInputEnabled(false)

        binding.pager.apply {
            adapter = viewPager2Adapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        binding.bottomtab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let{binding.pager.setCurrentItem(it, false)}
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        TabLayoutMediator(binding.bottomtab, binding.pager) { tab, position ->

            val tabicons = listOf(ContextCompat.getDrawable(this, R.drawable.home), ContextCompat.getDrawable(this, R.drawable.calendar), ContextCompat.getDrawable(this, R.drawable.report), ContextCompat.getDrawable(this, R.drawable.mypage))
            val tabtext = listOf("홈", "가계부", "리포트", "마이페이지")

            tab.icon = tabicons[position]
            tab.text = tabtext[position]
        }.attach()
    }
}