package com.example.mymate.presentation.home.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.repository.home.MainHomeRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.home.MainHomeUseCase
import kotlinx.coroutines.launch

class MainHomeViewModel(application: Application): AndroidViewModel(application) {
    private val mainHomeUseCase = MainHomeUseCase(MainHomeRepository(DataStoreRepoUser(application.dataStore)))
    private val _dDay = MutableLiveData<String>()
    val dDay: LiveData<String> get() = _dDay

    private val _houseExpenseTotal = MutableLiveData<String>()
    val houseExpenseTotal: LiveData<String> get() = _houseExpenseTotal

    private val _houseExpenseRatioNotiText = MutableLiveData<String>()
    val houseExpenseRatioNotiText: LiveData<String> get() = _houseExpenseRatioNotiText

    private val _houseExpenseRatioText = MutableLiveData<String>()
    val houseExpenseRatioText: LiveData<String> get() = _houseExpenseRatioText

    private val _isHouseholdBudgetOver = MutableLiveData<Boolean>()
    val isHouseholdBudgetOver: LiveData<Boolean> get() = _isHouseholdBudgetOver

    private val _expenseGraphGuide = MutableLiveData<Float>()
    val expenseGraphGuide: LiveData<Float> get() = _expenseGraphGuide

    private val _expenseOverGuide = MutableLiveData<Float>()
    val expenseOverGuide: LiveData<Float> get() = _expenseOverGuide

    private val _expenseIndicatorGuide = MutableLiveData<Float>()
    val expenseIndicatorGuide: LiveData<Float> get() = _expenseIndicatorGuide

    private val _budgetOverWarn = MutableLiveData<String>()
    val budgetOverWarn: LiveData<String> get() = _budgetOverWarn

    private val _isNowBetter = MutableLiveData<Boolean>()
    val isNowBetter: LiveData<Boolean> get() = _isNowBetter

    private val _compareGuideTop = MutableLiveData<Float>()
    val compareGuideTop: LiveData<Float> get() = _compareGuideTop

    private val _compareGuideMid = MutableLiveData<Float>()
    val compareGuideMid: LiveData<Float> get() = _compareGuideMid

    private val _periodPassed = MutableLiveData<String>()
    val periodPassed: LiveData<String> get() = _periodPassed

    private val _expenseCompared = MutableLiveData<String>()
    val expenseCompared: LiveData<String> get() = _expenseCompared

    private val _userExpenseLeft = MutableLiveData<String>()
    val userExpenseLeft: LiveData<String> get() = _userExpenseLeft

    private val _userExpenseUsed = MutableLiveData<String>()
    val userExpenseUsed: LiveData<String> get() = _userExpenseUsed

    private val _userBudget = MutableLiveData<String>()
    val userBudget: LiveData<String> get() = _userBudget

    private val _userExpenseLeftText = MutableLiveData<String>()
    val userExpenseLeftText: LiveData<String> get() = _userExpenseLeftText

    private val _userBudgetText = MutableLiveData<String>()
    val userBudgetText: LiveData<String> get() = _userBudgetText

    private val _isBadgeGone = MutableLiveData<Boolean>()
    val isBadgeGone: LiveData<Boolean> get() = _isBadgeGone

    init {
        getHomeInfo()
    }

    fun getHomeInfo() {
        viewModelScope.launch {
            val homeStats = mainHomeUseCase.getHomeStats()
            _dDay.value = homeStats.dDay
            _houseExpenseTotal.value = homeStats.houseExpenseTotal

            _houseExpenseRatioNotiText.value = homeStats.houseExpenseRatioNotiText
            _houseExpenseRatioText.value = homeStats.houseExpenseRatio
            _expenseOverGuide.value = homeStats.expenseOverGuide
            _expenseIndicatorGuide.value = homeStats.expenseIndicatorGuide
            _expenseGraphGuide.value = homeStats.expenseGraphGuide
            _isHouseholdBudgetOver.value = homeStats.isHouseholdBudgetOver
            _budgetOverWarn.value = homeStats.budgetOverWarn

            _periodPassed.value = homeStats.periodPassed
            _isNowBetter.value = homeStats.isNowBetter
            _compareGuideTop.value = homeStats.compareGuideTop
            _compareGuideMid.value = homeStats.compareGuideMid
            _expenseCompared.value = homeStats.expenseCompared

            _userExpenseLeft.value = homeStats.userExpenseLeft
            _userExpenseUsed.value = homeStats.userExpenseUsed
            _userBudget.value = homeStats.userBudget
            _userExpenseLeftText.value = "${homeStats.userExpenseLeft}원"
            _userBudgetText.value = "${homeStats.userBudget}원"

            _isBadgeGone.value = mainHomeUseCase.isExpenseNotiNew()
        }
    }
}