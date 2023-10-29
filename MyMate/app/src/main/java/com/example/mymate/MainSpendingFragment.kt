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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.MainSpendingFragmentBinding
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
    lateinit var calendar: Calendar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        calendarVal = CalendarValues()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainSpendingFragmentBinding.inflate(inflater, container, false)
        var expenseDetail = ArrayList<householdExpenseDetail>()

        //calendar settings
        var selectedDate = LocalDate.now()
        setCalendarView(selectedDate)
        
        when (selectedDate.dayOfWeek) {
            DayOfWeek.SUNDAY -> binding.spendingDay.text = "일요일"
            DayOfWeek.MONDAY -> binding.spendingDay.text = "월요일"
            DayOfWeek.TUESDAY -> binding.spendingDay.text = "화요일"
            DayOfWeek.WEDNESDAY -> binding.spendingDay.text = "수요일"
            DayOfWeek.THURSDAY -> binding.spendingDay.text = "목요일"
            DayOfWeek.FRIDAY -> binding.spendingDay.text = "금요일"
            DayOfWeek.SATURDAY -> binding.spendingDay.text = "토요일"
        }

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

        setDailyExpenceView(expenseDetail)

        return binding.root
    }

    private fun setDailyExpenceView(detail: ArrayList<householdExpenseDetail>) {
        //recyclerview list
        val adapter = SpendingAdapter(mainActivity, detail)
        binding.dailySpendings.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCalendarView(date: LocalDate) {
        //calender header
        binding.monthText.text = monthTextFormatting(date)
        //generate date lists
        iteminfo = arrayListOf<calendarItem>()
        val dayList = dayInMonthArray(date)
        //recyclerview setting
        val adapter = CalendarAdapter(mainActivity, dayList, iteminfo, calendarVal)
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthTextFormatting(date: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("MM월")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        if (date == LocalDate.now()) {
            nowdate = date.format(dayformat).toInt()
            calendarVal.firstDay = nowdate -1
            calendarVal.lastDay = nowdate -1
        }
        if (firstDay.dayOfWeek == DayOfWeek.SUNDAY) {
            for (i in 1 .. yearMonth.lengthOfMonth()) {
                if (nowdate == i) {
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(true, false, false))
                } else {
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(false, false, false))
                }
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
                    dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                    iteminfo.add(calendarItem(false, false, false))
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