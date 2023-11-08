package com.example.mymate

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key)) //TODO: 이 코드는 카카오 SDK 초기화 코드로, 후에 스플래시액티비티로 옮길 것
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var repouser = DataStoreRepoUser(dataStore)
        val keyhash = Utility.getKeyHash(this)
        Log.d ("Hash", keyhash)
        initViewPager()
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "MyMate")
        //권한 설정(리팩토링 필요)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            var permission = mutableMapOf<String, String>()
            permission["notification"] = android.Manifest.permission.POST_NOTIFICATIONS
            requestPermissions(permission.values.toTypedArray(), 99)
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("permissions: notification", "PERMISSION_GRANTED")
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            var permission = mutableMapOf<String, String>()
            permission["camera"] = Manifest.permission.CAMERA
            requestPermissions(permission.values.toTypedArray(), 98)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d("permissions: camera", "PERMISSION_GRANTED")
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            var permission = mutableMapOf<String, String>()
            permission["readexternalstorage"] = Manifest.permission.READ_EXTERNAL_STORAGE
            requestPermissions(permission.values.toTypedArray(), 97)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("permissions: read storage", "PERMISSION_GRANTED")
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            var permission = mutableMapOf<String, String>()
            permission["writeexternalstorage"] = Manifest.permission.WRITE_EXTERNAL_STORAGE
            requestPermissions(permission.values.toTypedArray(), 96)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("permissions: write storage", "PERMISSION_GRANTED")
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            var permission = mutableMapOf<String, String>()
            permission["manageexternalstorage"] = Manifest.permission.MANAGE_EXTERNAL_STORAGE
            requestPermissions(permission.values.toTypedArray(), 95)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("permissions: manage storage", "PERMISSION_GRANTED")
            }
        }
        //화면 로그
        val display = this.applicationContext?.resources?.displayMetrics
        val deviceWidth = display?.widthPixels
        val deviceHeight = display?.heightPixels

        Log.d("devicesizewidth", "${pxtodp(deviceWidth!!, this)}")
        Log.d("devicesizeheight", "${pxtodp(deviceHeight!!, this)}")
        //TODO: 로그인되어 있지 않다면 로그인 화면으로 보내기. 즉, 이 액티비티를 루트로 두고 로그인/로그아웃을 쌓는 형태.
    }

    private fun pxtodp(px: Int, context: Context): Float {
        return px / ((context.resources.displayMetrics.densityDpi.toFloat()) / DisplayMetrics.DENSITY_DEFAULT)
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