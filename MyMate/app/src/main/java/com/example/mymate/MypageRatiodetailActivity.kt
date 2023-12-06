package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import com.example.mymate.databinding.ActivityMypageRatiodetailBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageRatiodetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageRatiodetailBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageRatiodetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val ratio = intent.getStringExtra("ratio")
        binding.ratio.text = ratio
        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        val pieChart = binding.ratiochart
        pieChart.setUsePercentValues(true)
        var entries = ArrayList<PieEntry>()
        val color = ArrayList<Int>()
        color.add(ContextCompat.getColor(context, R.color.purplemute_background))
        color.add(ContextCompat.getColor(context, R.color.pie_gray1))
        color.add(ContextCompat.getColor(context, R.color.pie_gray2))
        color.add(ContextCompat.getColor(context, R.color.pie_gray3))
        val colorItem = ArrayList<Int>()

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getHouseRatio::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getHouseRatio("Bearer $accessToken").enqueue(object : Callback<houseRatioResponse> {
            override fun onResponse(
                call: Call<houseRatioResponse>,
                response: Response<houseRatioResponse>
            ) {
                if (response.isSuccessful) {
                    val houseList = response.body()!!.data.household_members
                    /*for (i in 0 until houseList.size) {
                        entries.add(PieEntry(houseList[i].user_settlement_ratio.toFloat(), houseList[i].user_nickname))
                        colorItem.add(color[i%4])
                    }*/
                    // 전시를 위한 변경
                    entries.add(PieEntry(houseList[1].user_settlement_ratio.toFloat(), houseList[1].user_nickname))
                    entries.add(PieEntry(houseList[0].user_settlement_ratio.toFloat(), houseList[0].user_nickname))
                    entries.add(PieEntry(houseList[2].user_settlement_ratio.toFloat(), houseList[2].user_nickname))
                    entries.add(PieEntry(houseList[3].user_settlement_ratio.toFloat(), houseList[3].user_nickname))
                    colorItem.add(color[0])
                    colorItem.add(color[1])
                    colorItem.add(color[2])
                    colorItem.add(color[3])
                    binding.member1.text = houseList[1].user_nickname
                    binding.member2.text = houseList[0].user_nickname
                    binding.member3.text = houseList[2].user_nickname
                    binding.member4.text = houseList[3].user_nickname
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorItem
                        setDrawValues(false)
                    }
                    pieChart.apply {
                        data = PieData(pieDataSet)
                        description.isEnabled = false
                        isRotationEnabled = false
                        holeRadius = 85.toFloat()
                        transparentCircleRadius = 0f
                        legend.isEnabled = false
                        setTouchEnabled(false)
                        setDrawSliceText(false)
                    }
                    pieChart.invalidate()
                } else {
                    pieChart.isInvisible = true
                }
            }

            override fun onFailure(call: Call<houseRatioResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(정산 비율)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.ratioEdit.setOnClickListener {
            startActivity(Intent(context, MypageEditratioActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val pieChart = binding.ratiochart
        pieChart.setUsePercentValues(true)
        var entries = ArrayList<PieEntry>()
        val color = ArrayList<Int>()
        color.add(ContextCompat.getColor(context, R.color.purplemute_background))
        color.add(ContextCompat.getColor(context, R.color.pie_gray1))
        color.add(ContextCompat.getColor(context, R.color.pie_gray2))
        color.add(ContextCompat.getColor(context, R.color.pie_gray3))
        val colorItem = ArrayList<Int>()

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getHouseRatio::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getHouseRatio("Bearer $accessToken").enqueue(object : Callback<houseRatioResponse> {
            override fun onResponse(
                call: Call<houseRatioResponse>,
                response: Response<houseRatioResponse>
            ) {
                if (response.isSuccessful) {
                    val houseList = response.body()!!.data.household_members
                    /*for (i in 0 until houseList.size) {
                        entries.add(PieEntry(houseList[i].user_settlement_ratio.toFloat(), houseList[i].user_nickname))
                        colorItem.add(color[i%4])
                    }*/
                    // 전시를 위한 변경
                    entries.add(PieEntry(houseList[1].user_settlement_ratio.toFloat(), houseList[1].user_nickname))
                    entries.add(PieEntry(houseList[0].user_settlement_ratio.toFloat(), houseList[0].user_nickname))
                    entries.add(PieEntry(houseList[2].user_settlement_ratio.toFloat(), houseList[2].user_nickname))
                    entries.add(PieEntry(houseList[3].user_settlement_ratio.toFloat(), houseList[3].user_nickname))
                    colorItem.add(color[0])
                    colorItem.add(color[1])
                    colorItem.add(color[2])
                    colorItem.add(color[3])
                    binding.member1.text = houseList[1].user_nickname
                    binding.member2.text = houseList[0].user_nickname
                    binding.member3.text = houseList[2].user_nickname
                    binding.member4.text = houseList[3].user_nickname
                    binding.ratio.text = houseList[1].user_settlement_ratio + "%"
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorItem
                        setDrawValues(false)
                    }
                    pieChart.apply {
                        data = PieData(pieDataSet)
                        description.isEnabled = false
                        isRotationEnabled = false
                        holeRadius = 85.toFloat()
                        transparentCircleRadius = 0f
                        legend.isEnabled = false
                        setTouchEnabled(false)
                        setDrawSliceText(false)
                    }
                    pieChart.invalidate()
                } else {
                    pieChart.isInvisible = true
                }
            }

            override fun onFailure(call: Call<houseRatioResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(정산 비율)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}