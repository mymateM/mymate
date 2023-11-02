package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.MainSpendingFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.checkerframework.common.subtyping.qual.Bottom
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar

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

    private var year = ""
    private var month = ""
    private var day = ""
    private var selectedDate = LocalDate.now()
    private var expenseDetail = ArrayList<householdExpenseDetail>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        calendarVal = CalendarValues()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        binding.toBills.setOnClickListener {
            startActivity(Intent(mainActivity, BillManagerActivity::class.java))
        }

        binding.toAlarm.setOnClickListener {
            startActivity(Intent(mainActivity, AlarmActivity::class.java))
        }

        binding.toSearch.setOnClickListener {
            startActivity(Intent(mainActivity, SearchActivity::class.java))
        }

        binding.selectdate.setOnClickListener {

        }

        return binding.root
    }

    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.datepicker.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true

        binding.selectdate.setOnClickListener {
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

    private fun setDailyExpenceView(detail: ArrayList<householdExpenseDetail>, date: LocalDate) {
        //recyclerview list
        val adapter = SpendingAdapter(mainActivity, detail)
        binding.dailySpendings.adapter = adapter
    }

    private fun setCalendarView(date: LocalDate) {
        //calender header
        binding.monthText.text = monthTextFormatting(date)
        val exactdate = date
        //generate date lists
        iteminfo = arrayListOf<calendarItem>()
        val dayList = dayInMonthArray(date)
        //recyclerview setting
        val adapter = CalendarAdapter(mainActivity, dayList, iteminfo, calendarVal, date)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(mainActivity, 7)
        binding.mainCalendar.layoutManager = manager
        binding.mainCalendar.adapter = adapter.apply {
            setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
                override fun onItemClick(item: calendarItem, position: Int) {
                    when (position % 7) {
                        0 -> binding.spendingDay.text = "일요일"
                        1 -> binding.spendingDay.text = "월요일"
                        2 -> binding.spendingDay.text = "화요일"
                        3 -> binding.spendingDay.text = "수요일"
                        4 -> binding.spendingDay.text = "목요일"
                        5 -> binding.spendingDay.text = "금요일"
                        6 -> binding.spendingDay.text = "토요일"
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
        setDailyExpenceView(expenseDetail, selectedDate)
    }

    private fun monthTextFormatting(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("MM월")
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}