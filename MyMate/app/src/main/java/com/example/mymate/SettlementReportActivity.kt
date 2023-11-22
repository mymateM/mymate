package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySettlementReportBinding
import com.example.mymate.databinding.MainReportHouseholdFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SettlementReportActivity: AppCompatActivity() {
    lateinit var binding: ActivitySettlementReportBinding
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context

    private val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val householdFragment = SettlementReportHouseholdFragment()
    val meFragment = SettlementReportMeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettlementReportBinding.inflate(layoutInflater)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        binding.backbtn.setOnClickListener {
            finish()
        }

        initViewPager()

        setContentView(binding.root)
    }

    private fun initViewPager() {
        val pager2Adapter = viewPager2Adapter(this)
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

        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        val dateEndpoint = retrofit?.create(getSettlementDate::class.java)

        dateEndpoint!!.getSettlementDate("Bearer $accessToken").enqueue(object :
            Callback<settlementDateResponse> {
            override fun onResponse(
                call: Call<settlementDateResponse>,
                response: Response<settlementDateResponse>
            ) {
                if(response.isSuccessful) {
                    var date = response.body()!!.data
                    if (LocalDate.now().dayOfMonth < date.toInt()) {
                        //var thisdate = LocalDate.now().minusMonths(2)
                        var thisdate = LocalDate.now().minusMonths(1)
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = "${thisdate.monthValue}월 ${thisdate.dayOfMonth + 1}일 - ${thisdate.monthValue + 1}월 ${thisdate.dayOfMonth}일"
                        binding.thisperiod.text = thisperiod
                    } else {
                        var thisdate = LocalDate.now().minusMonths(1)
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = "${thisdate.monthValue}월 ${thisdate.dayOfMonth + 1}일 - ${thisdate.monthValue + 1}월 ${thisdate.dayOfMonth}일"
                        binding.thisperiod.text = thisperiod
                    }
                }
            }

            override fun onFailure(call: Call<settlementDateResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(리포트-정산일)", Toast.LENGTH_SHORT).show()
            }

        })
    }
}