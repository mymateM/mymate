package com.example.mymate

import android.content.Context
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
        color.add(ContextCompat.getColor(context, R.color.pie_green))
        color.add(ContextCompat.getColor(context, R.color.pie_pink))
        color.add(ContextCompat.getColor(context, R.color.pie_blue))
        color.add(ContextCompat.getColor(context, R.color.pie_yellow))
        color.add(ContextCompat.getColor(context, R.color.pie_purple))
        color.add(ContextCompat.getColor(context, R.color.pie_red))
        color.add(ContextCompat.getColor(context, R.color.pie_gray))
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
                    Log.d("MYPAGERATIO!!!", houseList.size.toString())
                    for (i in 0 until houseList.size) {
                        entries.add(PieEntry(houseList[i].user_settlement_ratio.toFloat(), houseList[i].user_nickname))
                        colorItem.add(color[i%7])
                    }
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorItem
                        valueTextColor = ContextCompat.getColor(context, android.R.color.transparent)
                        setDrawValues(false)
                    }
                    pieChart.apply {
                        data = PieData(pieDataSet)
                        description.isEnabled = false
                        isRotationEnabled = false
                        holeRadius = 85.45.toFloat()
                        transparentCircleRadius = 0f
                    }
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