package com.example.mymate.presentation.expense.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.repository.expense.SearchRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.expense.SearchUseCase
import com.example.mymate.presentation.expense.CalendarValues
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val searchUseCase = SearchUseCase(SearchRepository(DataStoreRepoUser(application.dataStore)))

    private val _amountText = MutableLiveData<String>()
    val amountText: LiveData<String> get() = _amountText

    private val _categoryText = MutableLiveData<String>()
    val categoryText: LiveData<String> get() = _categoryText

    private val _sortText = MutableLiveData<String>()
    val sortText: LiveData<String> get() = _sortText

    private val _periodText = MutableLiveData<String>()
    val periodText: LiveData<String> get() = _periodText

    private val _calendarValue = MutableLiveData<CalendarValues>()
    val calendarValue: LiveData<CalendarValues> get() = _calendarValue

    private val _dayList = MutableLiveData<ArrayList<LocalDate?>>()
    val dayList: LiveData<ArrayList<LocalDate?>> get() = _dayList

    private val _searchTrigger = MutableLiveData<Boolean>()
    val searchTrigger: LiveData<Boolean> get() = _searchTrigger //변화하는 걸 넙죽 받아먹고 계속 search() 호출하지 않도록

    fun setCalendar(day: Int? = null) {
        if (day == null) {
            _calendarValue.value?.init() ?: run { _calendarValue.value = CalendarValues() }
            return
        }
        _calendarValue.value?.setDay(day)
    }

    fun search() {

    }

    //TODO: trigger 조건 설정, search 함수 호출, recyclerview용 data 설정
    //TODO: view 파일에서 한 binding 요소가 다른 binding 요소 너무 많이 건드리지 않도록 하기
}