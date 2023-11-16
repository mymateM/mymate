package com.example.mymate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mymate.databinding.ActivityAlarmBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

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

        binding.tabBadge1.isGone = true

        initViewPager()
    }

    private fun initViewPager() {
        var pager2Adapter = viewPager2Adapter(this)
        val actFragment = AlarmActFragment()
        val spdFragment = AlarmSpdFragment()
        pager2Adapter.addFragment(actFragment)
        pager2Adapter.addFragment(spdFragment)

        val retrofit = RetrofitClientInstance.client
        val readEndpoint = retrofit?.create(readExpenseNoti::class.java)
        val userRepo = DataStoreRepoUser(dataStore)
        val endpoint = retrofit?.create(getExpenseNoti::class.java)
        var accesstoken = ""
        var notiResponse: expenseNotiResponse
        var notiList = ArrayList<expenseNoti>()
        val alarmList = ArrayList<ArrayList<expenseNoti>>()

        runBlocking {
            accesstoken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.expenseNoti("Bearer $accesstoken").enqueue(object:
            Callback<expenseNotiResponse> {
            override fun onResponse(
                call: Call<expenseNotiResponse>,
                response: Response<expenseNotiResponse>
            ) {
                notiResponse = response.body()!!
                notiList = notiResponse.data.notification_expenses

                binding.tabBadge1.isGone = notiList[0].is_read
            }

            override fun onFailure(call: Call<expenseNotiResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "연결 실패(지출)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.alarmpager.offscreenPageLimit = 2
        binding.alarmpager.apply {
            adapter = pager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 1) {
                        endpoint!!.expenseNoti("Bearer $accesstoken").enqueue(object:
                            Callback<expenseNotiResponse> {
                            override fun onResponse(
                                call: Call<expenseNotiResponse>,
                                response: Response<expenseNotiResponse>
                            ) {
                                notiResponse = response.body()!!
                                notiList = notiResponse.data.notification_expenses

                                readEndpoint!!.readExpenseNoti("Bearer $accesstoken", notiList[0].expense_notification_id).enqueue(object: Callback<defaultResponse> {
                                    override fun onResponse(
                                        call: Call<defaultResponse>,
                                        response: Response<defaultResponse>
                                    ) {

                                    }

                                    override fun onFailure(
                                        call: Call<defaultResponse>,
                                        t: Throwable
                                    ) {
                                        Toast.makeText(context, "연결 실패(지출-읽기)", Toast.LENGTH_SHORT).show()
                                    }

                                })
                            }

                            override fun onFailure(call: Call<expenseNotiResponse>, t: Throwable) {
                                Toast.makeText(context, "연결 실패(지출)", Toast.LENGTH_SHORT).show()
                            }
                        })
                        binding.tabBadge1.isGone = true
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