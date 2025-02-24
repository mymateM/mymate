package com.example.mymate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.data.dto.expense.CalendarInfo
import com.example.mymate.data.dto.expense.CalendarWrapper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class MainSpendingViewModel(application: Application): AndroidViewModel(application) {
    private val mainSpendingUseCase = MainSpendingUseCase(MainSpendingRepository(DataStoreRepoUser(application.dataStore)))
    private val _monthText = MutableLiveData<String>()
    val monthText: LiveData<String> get() = _monthText

    private val _yearText = MutableLiveData<String>()
    val yearText: LiveData<String> get() = _yearText

    private val _weekDayText = MutableLiveData<String>()
    val weekDayText: LiveData<String> get() = _weekDayText

    private val _today = MutableLiveData<String>()
    val today: LiveData<String> get() = _today

    private val _calendarInfo = MutableLiveData<ArrayList<CalendarInfo>>()
    val calendarInfo: LiveData<ArrayList<CalendarInfo>> get() = _calendarInfo

    init {
        setDate(LocalDate.now())
    }

    fun setDate(date: LocalDate) {
        viewModelScope.launch {
            _monthText.value = date.monthValue.toString() + "월"
            _yearText.value = date.year.toString()
            _weekDayText.value = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
            _today.value = date.monthValue.toString() + "월" + date.dayOfMonth.toString() + "일"
            _calendarInfo.value = mainSpendingUseCase.getCalendarInfo(date)
        }
    }

    fun parseDate(year: Int, month: Int, day: Int): LocalDate {
        val monthText = if (month < 10) { "0${month}" } else { (month).toString() }
        val dayText = if (day < 10) { "0$day" } else { day.toString() }
        return LocalDate.parse("$year-$monthText-$dayText")
    }

    fun getCalendarDays(date: LocalDate): ArrayList<Int> {
        val start = date.withDayOfMonth(1).dayOfWeek.value + 1
        val days = ArrayList<Int>()
        for (i in 1 until start) {
            days.add(0)
        }
        for (i in 1 .. YearMonth.from(date).lengthOfMonth()) {
            days.add(i)
        }
        val len = days.size
        for (i in len .. 35) {
            days.add(0)
        }
        return days
    }

    fun getCalendarInfo(date: LocalDate): ArrayList<CalendarInfo> {
        viewModelScope.launch {
            _calendarInfo.value = mainSpendingUseCase.getCalendarInfo(date)
            return@launch
        }
        return _calendarInfo.value!!
    }
}