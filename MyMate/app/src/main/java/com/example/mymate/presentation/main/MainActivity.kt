package com.example.mymate.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mymate.*
import com.example.mymate.databinding.ActivityMainBinding
import com.example.mymate.presentation.home.MainHomeFragment
import com.example.mymate.presentation.home.MainMypageFragment
import com.example.mymate.presentation.home.MainReportFragment
import com.example.mymate.presentation.home.MainSpendingFragment
import com.example.mymate.util.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    private val homeFragment = MainHomeFragment()
    private val spendingFragment = MainSpendingFragment()
    private val reportFragment = MainReportFragment()
    private val mypageFragment = MainMypageFragment()
    private val tabFragments = listOf(homeFragment, spendingFragment, reportFragment, mypageFragment)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //KakaoSdk.init(this, getString(R.string.kakao_native_app_key)) //카카오 SDK 초기화 코드
        requestWindowFeature(Window.FEATURE_NO_TITLE) // 프로그램 제목 표시줄 없애기
        supportActionBar?.hide() // 액션 바 없애기
        var repouser = DataStoreRepoUser(dataStore)
        //NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "MyMate") //네이버 SDK 초기화 코드
        var accessToken = ""
        /*runBlocking {
            accessToken = repouser.userAccessReadFlow.first().toString()
        }
        if (accessToken.isBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }*/
        requestPermissions()
        initViewPager()
        Glide.with(this).load(R.raw.mymate_splash).override(500, 500).into(binding.splashicon)
        setContentView(binding.root)
        loading()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        if (spendingFragment.resumed != "00") {
            spendingFragment.onResume()
        }
        if (homeFragment.resumed != "00") {
            homeFragment.onResume()
        }
        if (reportFragment.resumed != "00") {
            reportFragment.onResume()
        }
        if (mypageFragment.resumed != "00") {
            mypageFragment.onResume()
        }
    }

    private fun loading() { // 로드 완료 확인하면 스플래시화면 없애기
        if (homeFragment.resumed != "00") {
            binding.splash.isGone = true
            binding.splashicon.isGone = true
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                loading()
            }, 500)
        }
    }

    private fun initViewPager() { // 탭 프래그먼트 연결
        val pager2Adapter = ViewPager2Adapter(this)
        for (fragments in tabFragments) {
            pager2Adapter.addFragment(fragments)
        }

        binding.mainpager.offscreenPageLimit = 4
        binding.mainpager.isUserInputEnabled = false

        binding.mainpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        val tabicons = listOf(ContextCompat.getDrawable(this, R.drawable.home_default), ContextCompat.getDrawable(this,
            R.drawable.ledger_default
        ), ContextCompat.getDrawable(this, R.drawable.report_default), ContextCompat.getDrawable(this,
            R.drawable.mypage_default
        ))
        val tabtext = listOf("홈", "가계부", "리포트", "마이페이지")

        binding.mainbottomtab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {binding.mainpager.setCurrentItem(it, false)}
                tab?.icon?.setTint(getColor(R.color.purplemute_background)) //선택된 아이콘 컬러 변경
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(getColor(R.color.gray_tab)) //선택되지 않은 아이콘 컬러 변경
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(binding.mainbottomtab, binding.mainpager) {tab, position ->
            tab.icon = tabicons[position]
            tab.text = tabtext[position]
        }.attach()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)  {
            var permission = mutableMapOf<String, String>()
            permission["notification"] = Manifest.permission.POST_NOTIFICATIONS
            permission["camera"] = Manifest.permission.CAMERA
            permission["readExternalStorage"] = Manifest.permission.READ_EXTERNAL_STORAGE
            permission["writeExternalStorage"] = Manifest.permission.WRITE_EXTERNAL_STORAGE
            permission["manageExternalStorage"] = Manifest.permission.MANAGE_EXTERNAL_STORAGE
            for (i in permission) {
                if (ContextCompat.checkSelfPermission(this, i.value) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(i.value), 1001)
                }
            }
        }
    }

}