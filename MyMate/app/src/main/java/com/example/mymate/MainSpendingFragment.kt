package com.example.mymate

import android.content.Context
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class calendarItem (
    var startorEnd: Boolean = false,
    var middle: Boolean = false
)

class MainSpendingFragment : Fragment() {
    lateinit var binding: MainSpendingFragmentBinding
    lateinit var mainActivity: MainActivity
    private var iteminfo = arrayListOf<calendarItem>()
    lateinit var calendarVal: CalendarValues

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

        //calendar settings
        var selectedDate = LocalDate.now()
        setCalendarView(selectedDate)

        //button events
        binding.monthLast.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setCalendarView(selectedDate)
        }

        binding.monthLast.setOnClickListener {
            selectedDate.plusMonths(1)
            setCalendarView(selectedDate)
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCalendarView(date: LocalDate) {
        //calender header
        binding.monthText.text = monthTextFormatting(date)
        //generate date lists
        val dayList = dayInMonthArray(date)

        //recyclerview setting
        val adapter = CalendarAdapter(mainActivity, dayList, iteminfo, calendarVal)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(mainActivity, 7)
        binding.mainCalendar.layoutManager = manager
        binding.mainCalendar.adapter = adapter.apply {
            setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
                override fun onItemClick(item: calendarItem, position: Int) {
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT)
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
        for (i in 1..41) {
            if(i <= dayOfWeek || i > (lastDay + dayOfWeek)) {
                dayList.add(null)
                iteminfo.add(calendarItem())
            } else {
                dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                iteminfo.add(calendarItem())
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