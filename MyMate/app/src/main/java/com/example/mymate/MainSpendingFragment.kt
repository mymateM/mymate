package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mymate.databinding.MainSpendingFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import org.checkerframework.common.subtyping.qual.Bottom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

data class calendarItem (
    var startorEnd: Boolean = false,
    var middle: Boolean = false,
    var lastornext: Boolean = false
)

class MainSpendingFragment : Fragment() {
    lateinit var binding: MainSpendingFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var iteminfo: ArrayList<calendarItem>
    lateinit var calendarVal: CalendarValues
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var userRepo: DataStoreRepoUser
    lateinit var montBoldTypeface: Typeface
    lateinit var suitBoldTypeface: Typeface

    private var year = ""
    private var month = ""
    private var day = ""
    lateinit var selectedDate: LocalDate
    private var expenseDetail = ArrayList<ExpenseList>()
    var resumed = "00"

    var retrofit = RetrofitClientInstance.client
    var endpoint = retrofit?.create(getDailyExpense::class.java)
    
    private var formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        calendarVal = CalendarValues()
        selectedDate = LocalDate.now()
        userRepo = DataStoreRepoUser(context.dataStore)
        montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainSpendingFragmentBinding.inflate(inflater, container, false)
        bottomSheetInit()
        //modaleimg settings
        binding.cover.isGone = true

        //calendar settings
        setCalendarView(selectedDate)

        //button events
        binding.lastMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }

        binding.nextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }

        binding.bills.setOnClickListener {
            startActivity(Intent(mainActivity, BillManagerActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.alarm.setOnClickListener {
            startActivity(Intent(mainActivity, AlarmActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.search.setOnClickListener {
            startActivity(Intent(mainActivity, SearchActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.spendingPlus.setOnClickListener {
            val toSpendingPlus = Intent(mainActivity, SpendingAddActivity::class.java)
            toSpendingPlus.putExtra("thedate", formatter.format(selectedDate))
            startActivity(Intent(toSpendingPlus))
        }
        resumed = "01"
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.datepicker.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true

        binding.selectdate.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.yeartext.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.selectdatecontainer.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.monthText.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.datepicker.confirmbtn.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            getDate()
            when (selectedDate.dayOfWeek) {
                DayOfWeek.SUNDAY -> binding.spendingDay.text = "일요일"
                DayOfWeek.MONDAY -> binding.spendingDay.text = "월요일"
                DayOfWeek.TUESDAY -> binding.spendingDay.text = "화요일"
                DayOfWeek.WEDNESDAY -> binding.spendingDay.text = "수요일"
                DayOfWeek.THURSDAY -> binding.spendingDay.text = "목요일"
                DayOfWeek.FRIDAY -> binding.spendingDay.text = "금요일"
                DayOfWeek.SATURDAY -> binding.spendingDay.text = "토요일"
            }
            binding.cover.isGone = true
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getDate() {
        val tempmonth = binding.datepicker.spinnerpicker.month + 1
        month = if (tempmonth < 10) {
            "0$tempmonth"
        } else {
            tempmonth.toString()
        }
        day = if (binding.datepicker.spinnerpicker.dayOfMonth < 10) {
            "0" + binding.datepicker.spinnerpicker.dayOfMonth.toString()
        } else {
            binding.datepicker.spinnerpicker.dayOfMonth.toString()
        }
        year = binding.datepicker.spinnerpicker.year.toString()

        selectedDate = LocalDate.parse("$year-$month-$day")
        setCalendarView(selectedDate)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDailyExpenceView(date: LocalDate) {
        var accessToken = ""
        var detailResponse: dailyExpenseResponse
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        month = if (date.monthValue < 10) {
            "0${date.monthValue}"
        } else {
            date.monthValue.toString()
        }
        day = if (date.dayOfMonth < 10) {
            "0" + date.dayOfMonth
        } else {
            date.dayOfMonth.toString()
        }
        val todaynoti = SpannableStringBuilder(date.monthValue.toString() + "월 " + date.dayOfMonth.toString() + "일")
        if (date.monthValue < 10 && date.dayOfMonth < 10) {
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), todaynoti.length - 2, todaynoti.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (date.monthValue < 10) {
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), todaynoti.length - 3, todaynoti.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (date.dayOfMonth < 10) {
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), todaynoti.length - 2, todaynoti.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            todaynoti.setSpan(TypefaceSpan(montBoldTypeface), todaynoti.length - 3, todaynoti.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.today.text = todaynoti
        year = date.year.toString()
        endpoint!!.getDailyExpense("Bearer $accessToken", year, month, day).enqueue(object : Callback<dailyExpenseResponse> {
            override fun onResponse(
                call: Call<dailyExpenseResponse>,
                response: Response<dailyExpenseResponse>
            ) {
                if (response.isSuccessful) {
                    detailResponse = response.body()!!
                    expenseDetail = detailResponse.data.expenses
                    val adapter = SpendingAdapter(mainActivity, expenseDetail)
                    val manager = LinearLayoutManager(mainActivity)
                    binding.dailySpendings.layoutManager = manager
                    binding.dailySpendings.adapter = adapter.apply {
                        setOnItemClickListener(object : SpendingAdapter.OnItemClickListener {
                            override fun onItemClick(item: ExpenseList, position: Int) {
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<dailyExpenseResponse>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패", Toast.LENGTH_SHORT).show()
                val adapter = SpendingAdapter(mainActivity, expenseDetail)
                val manager = LinearLayoutManager(mainActivity)
                binding.dailySpendings.layoutManager = manager
                binding.dailySpendings.adapter = adapter
                }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setCalendarView(date: LocalDate) {
        //calender header
        val monthText = SpannableStringBuilder(monthTextFormatting(date))
        monthText.setSpan(TypefaceSpan(montBoldTypeface), 0, monthText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.monthText.text = monthText
        val yearText = SpannableStringBuilder(yearTextFormatting(date))
        binding.yeartext.text = yearText
        //generate date lists
        iteminfo = arrayListOf<calendarItem>()
        val dayList = dayInMonthArray(date)
        //item info comms
        var calendarendpoint = retrofit?.create(getCalendar::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        calendarendpoint!!.getCalendar("Bearer $accessToken", date.year.toString(), date.monthValue.toString(), date.dayOfMonth.toString()).enqueue(object : Callback<calendarResponse> {
            override fun onResponse(
                call: Call<calendarResponse>,
                response: Response<calendarResponse>
            ) {
                if (response.isSuccessful) {
                    var spendList = response.body()!!.data
                    val adapter = CalendarAdapter(mainActivity, dayList, iteminfo, calendarVal, spendList)
                    var manager: RecyclerView.LayoutManager = GridLayoutManager(mainActivity, 7)
                    binding.mainCalendar.layoutManager = manager
                    binding.mainCalendar.adapter = adapter.apply {
                        setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
                            @RequiresApi(Build.VERSION_CODES.P)
                            override fun onItemClick(item: calendarItem, position: Int, day: LocalDate?) {
                                when (position % 7) {
                                    0 -> binding.spendingDay.text = "일요일"
                                    1 -> binding.spendingDay.text = "월요일"
                                    2 -> binding.spendingDay.text = "화요일"
                                    3 -> binding.spendingDay.text = "수요일"
                                    4 -> binding.spendingDay.text = "목요일"
                                    5 -> binding.spendingDay.text = "금요일"
                                    6 -> binding.spendingDay.text = "토요일"
                                }
                                if (day != null) {
                                    setDailyExpenceView(day)
                                    selectedDate = day
                                }
                            }
                        })
                    }
                    when (selectedDate.dayOfWeek) {
                        DayOfWeek.SUNDAY -> binding.spendingDay.text = "일요일"
                        DayOfWeek.MONDAY -> binding.spendingDay.text = "월요일"
                        DayOfWeek.TUESDAY -> binding.spendingDay.text = "화요일"
                        DayOfWeek.WEDNESDAY -> binding.spendingDay.text = "수요일"
                        DayOfWeek.THURSDAY -> binding.spendingDay.text = "목요일"
                        DayOfWeek.FRIDAY -> binding.spendingDay.text = "금요일"
                        DayOfWeek.SATURDAY -> binding.spendingDay.text = "토요일"
                    }
                    setDailyExpenceView(selectedDate)
                } else {
                    Toast.makeText(context, "연결 오류(캘린더)", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<calendarResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(캘린더)", Toast.LENGTH_SHORT).show()
            }

        })
        //recyclerview setting

    }

    private fun monthTextFormatting(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("MM월")
        return date.format(formatter)
    }

    private fun yearTextFormatting(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("yyyy")
        return date.format(formatter)
    }

    private fun dayInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        var dayList = ArrayList<LocalDate?>()
        var yearMonth = YearMonth.from(date)
        var lastDay = yearMonth.lengthOfMonth()
        var firstDay = date.withDayOfMonth(1)
        var dayOfWeek = firstDay.dayOfWeek.value
        var nowdate: Int = 0
        var tempmonth = date
        var tempday = date
        var tempint = 0
        val dayformat = DateTimeFormatter.ofPattern("dd")
        nowdate = date.format(dayformat).toInt()
        if (firstDay.dayOfWeek == DayOfWeek.SUNDAY) {
            for (i in 1 .. yearMonth.lengthOfMonth()) {
                if (nowdate == i) {
                    calendarVal.firstDay = nowdate -1
                    calendarVal.lastDay = nowdate -1
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(true, false, false))
                } else {
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(false, false, false))
                }
            }
            for (i in 1 .. 11) {
                tempmonth = date.plusMonths(1)
                tempday = tempmonth.withDayOfMonth(1).plusDays(tempint.toLong())
                dayList.add(tempday)
                iteminfo.add(calendarItem(false, false, true))
                tempint++
            }
        } else {
            for (i in 1..42) {
                if(i <= dayOfWeek) {
                    tempmonth = date.minusMonths(1)
                    tempday = tempmonth.withDayOfMonth(tempmonth.lengthOfMonth()).minusDays(dayOfWeek.toLong() - i)
                    dayList.add(tempday)
                    iteminfo.add(calendarItem(false, false, true))
                } else if (i > lastDay + dayOfWeek) {
                    tempmonth = date.plusMonths(1)
                    tempday = tempmonth.withDayOfMonth(1).plusDays(tempint.toLong())
                    dayList.add(tempday)
                    iteminfo.add(calendarItem(false, false, true))
                    tempint++
                } else {
                    if (nowdate == (i - dayOfWeek)) {
                        calendarVal.firstDay = i - 1
                        calendarVal.lastDay = i - 1
                        Log.d("DATE", calendarVal.firstDay.toString())
                        dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                        iteminfo.add(calendarItem(true, false, false))
                    } else {
                        dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                        iteminfo.add(calendarItem(false, false, false))
                    }
                }
            }
        }

        return dayList
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        bottomSheetInit()

        //modaleimg settings
        binding.cover.isGone = true

        //calendar settings
        setCalendarView(selectedDate)

        //button events
        binding.monthLast.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }

        binding.monthNext.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }
    }
}