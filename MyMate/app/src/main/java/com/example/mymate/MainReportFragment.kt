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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.MainReportFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainReportFragment : Fragment() {
    lateinit var binding: MainReportFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var userRepo: DataStoreRepoUser

    val householdFragment = MainReportHouseholdFragment()
    val meFragment = MainReportMeFragment()
    val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var resumed = "00"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainReportFragmentBinding.inflate(inflater, container, false)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)

        initViewPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resumed = "01"
    }

    private fun initViewPager() {
        val pager2Adapter = viewPager2Adapter(this.requireActivity())
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

        dateEndpoint!!.getSettlementDate("Bearer $accessToken").enqueue(object : Callback<settlementDateResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<settlementDateResponse>,
                response: Response<settlementDateResponse>
            ) {
                if(response.isSuccessful) {
                    var date = response.body()!!.data
                    val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
                    if (LocalDate.now().dayOfMonth < date.toInt()) {
                        var thisdate = LocalDate.now().minusMonths(1)
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = SpannableStringBuilder("${thisdate.monthValue}월 ${thisdate.dayOfMonth}일 -")
                        thisperiod.setSpan(TypefaceSpan(montBoldTypeface), 0, thisdate.monthValue.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        thisperiod.setSpan(TypefaceSpan(montBoldTypeface), thisdate.monthValue.toString().length + 2, thisperiod.length - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.thisperiod.text = thisperiod
                    } else {
                        var thisdate = LocalDate.now()
                        thisdate = thisdate.withDayOfMonth(date.toInt())
                        date = thisdate.format(formatter)
                        val thisperiod = SpannableStringBuilder("${thisdate.monthValue}월 ${thisdate.dayOfMonth}일 -")
                        thisperiod.setSpan(TypefaceSpan(montBoldTypeface), 0, thisdate.monthValue.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        thisperiod.setSpan(TypefaceSpan(montBoldTypeface), thisdate.monthValue.toString().length + 2, thisperiod.length - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        binding.thisperiod.text = thisperiod
                    }
                }
            }

            override fun onFailure(call: Call<settlementDateResponse>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패(리포트-정산일)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
        initViewPager()
    }
}