package com.example.mymate

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key)) //TODO: 이 코드는 카카오 SDK 초기화 코드로, 후에 스플래시액티비티로 옮길 것
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val keyhash = Utility.getKeyHash(this)
        Log.d ("Hash", keyhash)
        initViewPager()
    }

    private fun initViewPager() {
        var pager2Adapter = viewPager2Adapter(this)
        pager2Adapter.addFragment(MainHomeFragment())
        pager2Adapter.addFragment(MainSpendingFragment())
        pager2Adapter.addFragment(MainReportFragment())
        pager2Adapter.addFragment(MainMypageFragment()) //홈화면 탭에 연결할 fragment를 adapter에 추가

        binding.mainpager.offscreenPageLimit = 4
        binding.mainpager.setUserInputEnabled(false)

        binding.mainpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        val tabicons = listOf(ContextCompat.getDrawable(this, R.drawable.icon_hometab), ContextCompat.getDrawable(this, R.drawable.icon_calendartab), ContextCompat.getDrawable(this, R.drawable.icon_reporttab), ContextCompat.getDrawable(this, R.drawable.icon_mypagetab))
        val tabtext = listOf("홈", "가계부", "리포트", "마이페이지")

        //TODO: tabicon color tint failed, try a solid change

        binding.mainbottomtab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {binding.mainpager.setCurrentItem(it, false)}
                tab?.icon?.setTint(getColor(R.color.purplevivid_graph)) //선택된 아이콘 컬러 변경
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(getColor(R.color.graymute_tab)) //선택되지 않은 아이콘 컬러 변경
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(binding.mainbottomtab, binding.mainpager) {tab, position ->
            tab.icon = tabicons[position]
            tab.text = tabtext[position]
        }.attach()
    }

}