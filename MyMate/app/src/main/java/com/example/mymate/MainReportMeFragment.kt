package com.example.mymate

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.MainReportMeFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class MainReportMeFragment: Fragment() {
    lateinit var binding: MainReportMeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var userRepo: DataStoreRepoUser

    private val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainReportMeFragmentBinding.inflate(inflater, container, false)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)

        initMe()

        return binding.root
    }

    private fun initMe() {
        val mybind = binding
        val dateEndpoint = retrofit?.create(getSettlementDate::class.java)
        val myreportEndpoint = retrofit?.create(getMyReport::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        dateEndpoint!!.getSettlementDate("Bearer $accessToken").enqueue(object: Callback<settlementDateResponse> {
            override fun onResponse(
                call: Call<settlementDateResponse>,
                response: Response<settlementDateResponse>
            ) {
                if (response.isSuccessful) {
                    var date = response.body()!!.data
                    if (LocalDate.now().dayOfMonth < date.toInt()) {
                        var thisdate = LocalDate.now().minusMonths(1)
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = "${thisdate.monthValue}월 ${thisdate.dayOfMonth}일 -"
                    } else {
                        var thisdate = LocalDate.now()
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = "${thisdate.monthValue}월 ${thisdate.dayOfMonth}일 -"
                    }
                    myreportEndpoint!!.getMyReport("Bearer $accessToken", date).enqueue(object : Callback<myReportResponse> {
                        @RequiresApi(Build.VERSION_CODES.P)
                        override fun onResponse(
                            call: Call<myReportResponse>,
                            response: Response<myReportResponse>
                        ) {
                            if (response.isSuccessful) {
                                val mydata = response.body()!!.data

                                val categoryName = ArrayList<String>()
                                val categoryRatio = ArrayList<Float>()
                                val categoryAbs = ArrayList<Int>()
                                var totalExpense = 0

                                for (i in 0 until mydata.expense_categories.size) {
                                    categoryName.add(mydata.expense_categories[i].category_name)
                                    categoryRatio.add(mydata.expense_categories[i].total_expense_ratio.toFloat())
                                    categoryAbs.add(mydata.expense_categories[i].total_expense_amount.toInt())
                                    totalExpense += mydata.expense_categories[i].total_expense_amount.toInt()
                                }

                                var maxCategory = ""
                                var maxPosition = -1
                                for (i in 0 until mydata.expense_categories.size) {
                                    if (i == 0) {
                                        maxPosition = 0
                                    }
                                    if (mydata.expense_categories[i].total_expense_ratio.toFloat() > mydata.expense_categories[maxPosition].total_expense_ratio.toFloat()) {
                                        maxPosition = i
                                    }
                                }
                                maxCategory = mydata.expense_categories[maxPosition].category_name
                                val categorytitle = SpannableStringBuilder("이 달의 많이 쓴 카테고리는\n${maxCategory}입니다")
                                val colorItemList = ArrayList<Int>()
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_yellow))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_red))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_blue))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_gray))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_green))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_pink))
                                colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_purple))
                                var categoryColor = 0
                                when(maxCategory) {
                                    "식비" -> categoryColor = colorItemList[0]
                                    "생활" -> categoryColor = colorItemList[6]
                                    "쇼핑" -> categoryColor = colorItemList[5]
                                    "교통" -> categoryColor = colorItemList[2]
                                    "의료" -> categoryColor = colorItemList[1]
                                    "고지서" -> categoryColor = colorItemList[0]
                                    "교육" -> categoryColor = colorItemList[4]
                                    "기타" -> categoryColor = colorItemList[3]
                                }
                                categorytitle.setSpan(ForegroundColorSpan(categoryColor), 16, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.categorytitle.text = categorytitle
                                val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
                                val suitMediumTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_medium), Typeface.NORMAL)
                                val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
                                val piemidtxt = SpannableStringBuilder("총 지출\n${digitprocessing(totalExpense.toString())}원")
                                piemidtxt.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.graydark_text)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                piemidtxt.setSpan(AbsoluteSizeSpan(16, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                piemidtxt.setSpan(TypefaceSpan(suitMediumTypeface), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                piemidtxt.setSpan(TypefaceSpan(suitBoldTypeface), piemidtxt.length - 1, piemidtxt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                binding.pieMidText.text = piemidtxt

                                initPieRecycle(categoryName, categoryRatio, categoryAbs, totalExpense)
                            }
                        }

                        override fun onFailure(call: Call<myReportResponse>, t: Throwable) {
                            Toast.makeText(mainActivity, "연결 실패(리포트-개인)", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

            override fun onFailure(call: Call<settlementDateResponse>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패(리포트-정산일)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initPieRecycle(categoryName: ArrayList<String>, categoryRatio: ArrayList<Float>, categoryAbs: ArrayList<Int>, total: Int) {
        //Piechart Setting
        val pieChart = binding.categorypie
        pieChart.setUsePercentValues(true)
        val entries = ArrayList<PieEntry>()
        val colorItemList = ArrayList<Int>()
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_yellow))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_red))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_blue))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_gray))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_green))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_pink))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_purple))
        val colorItem = ArrayList<Int>()
        for (i in 0 until categoryName.size) {
            entries.add(PieEntry(categoryRatio[i] * 100))
            when(categoryName[i]) {
                "식비" ->  colorItem.add(colorItemList[0])
                "생활" ->  colorItem.add(colorItemList[6])
                "쇼핑" ->  colorItem.add(colorItemList[5])
                "교통" ->  colorItem.add(colorItemList[2])
                "의료" ->  colorItem.add(colorItemList[1])
                "고지서" ->  colorItem.add(colorItemList[0])
                "교육" ->  colorItem.add(colorItemList[4])
                "기타" ->  colorItem.add(colorItemList[3])
            }
        }
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorItem
            setDrawValues(false)
        }
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            isRotationEnabled = false
            transparentCircleRadius = 0f
            holeRadius = 85f
            setHoleColor(ContextCompat.getColor(mainActivity, R.color.white))
            legend.isEnabled = false
            setTouchEnabled(false)
        }
        pieChart.invalidate()

        //RecyclerView SEtting
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(mainActivity)
        val adapter = MainReportListAdapter(categoryName, categoryRatio, categoryAbs, colorItemList)
        binding.mylist.adapter = adapter
        binding.mylist.layoutManager = manager
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }
}