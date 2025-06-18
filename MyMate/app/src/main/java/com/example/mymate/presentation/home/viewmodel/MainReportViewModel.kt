package com.example.mymate.presentation.home.viewmodel

import android.app.Application
import android.icu.text.DecimalFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.data.dto.report.UserReportProcessed
import com.example.mymate.data.repository.home.MainReportRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.home.MainReportUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainReportViewModel(application: Application): AndroidViewModel(application) {
    private val mainReportUseCase = MainReportUseCase(MainReportRepository(DataStoreRepoUser(application.dataStore)))
    private val _periodDate = MutableLiveData<LocalDate>()
    val periodDate: LiveData<LocalDate> get() = _periodDate

    private val _myCategoryTitle = MutableLiveData<String>()
    val myCategoryTitle: LiveData<String> get() = _myCategoryTitle

    private val _myTotalExpense = MutableLiveData<String>()
    val myTotalExpense: LiveData<String> get() = _myTotalExpense

    private val _myReport = MutableLiveData<UserReportProcessed>()
    val myReport: LiveData<UserReportProcessed> get() = _myReport

    private val _householdReport = MutableLiveData<HouseholdReportProcessed>()
    val householdReport: LiveData<HouseholdReportProcessed> get() = _householdReport

    private val _houseCategoryTitle = MutableLiveData<String>()
    val houseCategoryTitle: LiveData<String> get() = _houseCategoryTitle

    private val _houseTotalExpense = MutableLiveData<String>()
    val houseTotalExpense: LiveData<String> get() = _houseTotalExpense

    private val _houseIsOver = MutableLiveData<Boolean>()
    val houseIsOver: LiveData<Boolean> get() = _houseIsOver

    init {
        setPeriodDate()
        setUserData()
        setHouseholdData()
    }

    fun setPeriodDate() {
        viewModelScope.launch {
            _periodDate.value = mainReportUseCase.getPeriodDate()
        }
    }

    fun setUserData() {
        viewModelScope.launch {
            if (periodDate.value == null) {
                _myReport.value = mainReportUseCase.getUserReportData(LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
            } else {
                _myReport.value = mainReportUseCase.getUserReportData(periodDate.value!!.monthValue, periodDate.value!!.dayOfMonth)
            }
            if (myReport.value != null && myReport.value!!.categoryName.size > 0) {
                _myCategoryTitle.value = myReport.value!!.categoryName[myReport.value!!.maxCategoryIndex]
                _myTotalExpense.value = DecimalFormat("#,###").format(myReport.value!!.totalExpense)
            } else {
                _myCategoryTitle.value = ""
                _myTotalExpense.value = DecimalFormat("#,###").format(0)
            }
        }
    }

    fun setHouseholdData() {
        viewModelScope.launch {
            if (periodDate.value == null) {
                _householdReport.value = mainReportUseCase.getHouseholdReportData(LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
            } else {
                _householdReport.value = mainReportUseCase.getHouseholdReportData(periodDate.value!!.monthValue, periodDate.value!!.dayOfMonth)
            }
            if (householdReport.value != null && householdReport.value!!.categoryName.size > 0) {
                _houseCategoryTitle.value = householdReport.value!!.categoryName[householdReport.value!!.maxCategoryIndex]
                _houseTotalExpense.value = DecimalFormat("#,###").format(householdReport.value!!.totalExpense)
                _houseIsOver.value = householdReport.value!!.isOver
            } else {
                _houseCategoryTitle.value = ""
                _houseTotalExpense.value = DecimalFormat("#,###").format(0)
                _houseIsOver.value = false
            }
        }
    }
}