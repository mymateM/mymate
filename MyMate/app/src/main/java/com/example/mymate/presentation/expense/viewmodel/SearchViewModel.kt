package com.example.mymate.presentation.expense.viewmodel

import android.app.Application
import android.icu.text.DecimalFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.repository.expense.SearchRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.expense.SearchUseCase
import com.example.mymate.presentation.expense.CalendarValues
import com.example.mymate.util.Category
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val searchUseCase = SearchUseCase(SearchRepository(DataStoreRepoUser(application.dataStore)))

    private val _amountText = MutableLiveData<String>()
    val amountText: LiveData<String> get() = _amountText

    private val _categoryText = MutableLiveData<String>()
    val categoryText: LiveData<String> get() = _categoryText

    private val _categorySelection = MutableLiveData<ArrayList<Boolean>>()
    val categorySelection: LiveData<ArrayList<Boolean>> get() = _categorySelection

    private val _sortText = MutableLiveData<String>()
    val sortText: LiveData<String> get() = _sortText

    private val _periodText = MutableLiveData<String>()
    val periodText: LiveData<String> get() = _periodText

    private val _calendarValue = MutableLiveData<CalendarValues>()
    val calendarValue: LiveData<CalendarValues> get() = _calendarValue

    private val _dayList = MutableLiveData<ArrayList<LocalDate>>()
    val dayList: LiveData<ArrayList<LocalDate>> get() = _dayList

    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> get() = _selectedDate

    private val _searchList = MutableLiveData<ArrayList<ArrayList<ExpenseSummary>>>()
    val searchList: LiveData<ArrayList<ArrayList<ExpenseSummary>>> get() = _searchList

    init {
        initOption()
    }

    fun initOption() {
        viewModelScope.launch {
            _amountText.value = "가격"
            _sortText.value = "정렬"
            _periodText.value = "기간"
            _categoryText.value = "카테고리"
            _selectedDate.value = LocalDate.now()
            setDayList()
            val cList = ArrayList<Boolean>()
            for (i in 0 until 8) {
                cList.add(false)
            }
            _categorySelection.value = cList
            _calendarValue.value?.init() ?: run { _calendarValue.value = CalendarValues() }
            _searchList.value = ArrayList()
        }
    }

    fun setCalendar(day: Int? = null, selected: LocalDate? = null) {
        viewModelScope.launch {
            if (day == null) {
                _calendarValue.value?.init() ?: run { _calendarValue.value = CalendarValues() }
            } else {
                _calendarValue.value?.setDay(day)
                _calendarValue.value = _calendarValue.value!!.copy()
            }
            if (selected != null) {
                _selectedDate.value = selected
            }
        }
    }

    fun setOption(type: Int, index: Int? = null, minData: String? = null, maxData: String? = null) {
        viewModelScope.launch {
            when (type) {
                CATEGORY -> {
                    if (_categorySelection.value!![index!!]) {
                        _categorySelection.value!![index] = false
                        _categoryText.value = "카테고리"
                    } else {
                        val list = ArrayList<Boolean>()
                        for (i in 0 until 8) list.add(false)
                        list[index] = true
                        _categorySelection.value = list
                        _categoryText.value = Category.fromIndex(index).displayName
                    }
                }
                PERIOD -> {
                    val calendarVal = calendarValue.value!!
                    val textBuilder = StringBuilder()
                    if (calendarVal.firstDay >= 0) {
                        textBuilder.append(
                            dayList.value!!
                                .getOrNull(calendarVal.firstDay)
                                ?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                                .orEmpty()
                        )
                    }
                    if (calendarVal.lastDay >= 0) {
                        if (textBuilder.isNotEmpty()) textBuilder.append("-")
                        textBuilder.append(
                            dayList.value!!
                                .getOrNull(calendarVal.lastDay)
                                ?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                                .orEmpty()
                        )
                    }
                    if (textBuilder.isEmpty()) _periodText.value = "기간"
                    else _periodText.value = textBuilder.toString()
                }
                SORT -> {
                    if (index!! > 0) _sortText.value = "최신순"
                    else _sortText.value = "과거순"
                }
                AMOUNT -> {
                    val minInt = minData!!.toIntOrNull()
                    val maxInt = maxData!!.toIntOrNull()
                    val (minValue, maxValue) = when {
                        minInt != null && maxInt != null && minInt > maxInt -> maxInt to minInt
                        else -> minInt to maxInt
                    }

                    val formatter = DecimalFormat("#,###")
                    val minimum = minValue?.let { formatter.format(it) } ?: ""
                    val maximum = maxValue?.let { formatter.format(it) } ?: ""
                    _amountText.value = when {
                        minimum.isEmpty() && maximum.isEmpty() -> "가격"
                        minimum.isEmpty() -> "~ ${maximum}원"
                        maximum.isEmpty() -> "${minimum}원 ~"
                        else -> "${minimum}원 ~ ${maximum}원"
                    }
                }
            }
        }
    }

    fun setDayList() {
        viewModelScope.launch {
            val date = _selectedDate.value!!
            val dayListNew = ArrayList<LocalDate>()
            val lastDay = YearMonth.from(date).lengthOfMonth()
            val firstDay = date.withDayOfMonth(1)
            val dayOfWeek = firstDay.dayOfWeek.value
            val prevMonth = date.minusMonths(1)
            val prevMonthLastDay = prevMonth.lengthOfMonth()
            val nextMonth = date.plusMonths(1).withDayOfMonth(1)

            if (firstDay.dayOfWeek == DayOfWeek.SUNDAY) {
                for (i in 1 .. lastDay) {
                    dayListNew.add(LocalDate.of(date.year, date.monthValue, i))
                }
                val daysToFill = 42 - lastDay
                for (i in 0 until daysToFill) {
                    dayListNew.add(nextMonth.plusDays(i.toLong()))
                }
            } else {
                var overflowDayCounter = 0
                for (i in 1..42) {
                    when {
                        i <= dayOfWeek -> {
                            val day = prevMonthLastDay - (dayOfWeek - i)
                            dayListNew.add(prevMonth.withDayOfMonth(day))
                        }
                        i > lastDay + dayOfWeek -> {
                            dayListNew.add(nextMonth.plusDays(overflowDayCounter.toLong()))
                            overflowDayCounter++
                        }
                        else -> {
                            val currentDay = i - dayOfWeek
                            dayListNew.add(LocalDate.of(date.year, date.monthValue, currentDay))
                        }
                    }
                }
            }

            _dayList.value = dayListNew
        }
    }

    fun search() {
        viewModelScope.launch {
            _searchList.value = searchUseCase.search(
                _amountText.value ?: "",
                _sortText.value ?: "",
                _periodText.value ?: "",
                _categoryText.value?: ""
            )
        }
    }

    //TODO: trigger 조건 설정, search 함수 호출, recyclerview용 data 설정
    //TODO: view 파일에서 한 binding 요소가 다른 binding 요소 너무 많이 건드리지 않도록 하기

    companion object {
        const val AMOUNT = 1
        const val CATEGORY = 2
        const val SORT = 3
        const val PERIOD = 4
    }
}