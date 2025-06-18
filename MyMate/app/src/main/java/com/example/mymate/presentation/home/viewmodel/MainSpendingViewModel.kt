package com.example.mymate.presentation.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.repository.home.MainSpendingRepository
import com.example.mymate.domain.usecase.home.MainSpendingUseCase
import com.example.mymate.data.dto.expense.CalendarInfo
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.dataStore
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

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

    private val _expenseInfo = MutableLiveData<ArrayList<ExpenseSummary>>()
    val expenseInfo: LiveData<ArrayList<ExpenseSummary>> get() = _expenseInfo

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
            _expenseInfo.value = mainSpendingUseCase.getDailyExpenses(date)
        }
    }

    fun parseDate(year: Int, month: Int, day: Int): LocalDate {
        val monthText = if (month < 10) { "0${month}" } else { (month).toString() }
        val dayText = if (day < 10) { "0$day" } else { day.toString() }
        return LocalDate.parse("$year-$monthText-$dayText")
    }

    fun getCalendarInfo(date: LocalDate): ArrayList<CalendarInfo> {
        viewModelScope.launch {
            _calendarInfo.value = mainSpendingUseCase.getCalendarInfo(date)
            return@launch
        }
        return _calendarInfo.value!!
    }
}