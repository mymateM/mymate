package com.example.mymate.presentation.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.repository.home.MainMypageRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.home.MainMypageUseCase
import kotlinx.coroutines.launch

class MainMypageViewModel(application: Application): AndroidViewModel(application) {
    private val mainMypageUseCase = MainMypageUseCase(MainMypageRepository(DataStoreRepoUser(application.dataStore)))
    private val _profilePic = MutableLiveData<String>()
    val profilePic: LiveData<String> get() = _profilePic

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    private val _householdBudget = MutableLiveData<String>()
    val householdBudget: LiveData<String> get() = _householdBudget

    private val _settlementDate = MutableLiveData<String>()
    val settlementDate: LiveData<String> get() = _settlementDate

    private val _userPercentage = MutableLiveData<String>()
    val userPercentage: LiveData<String> get() = _userPercentage

    init {
        setUserInfo()
    }

    fun setUserInfo() {
        viewModelScope.launch {
            val rawdata = mainMypageUseCase.getUserInfo()
            _profilePic.value = rawdata.user_image_url
            _nickname.value = rawdata.user_nickname
            _householdBudget.value = rawdata.household_budget_amount
            _settlementDate.value = rawdata.household_settlement_date
            _userPercentage.value = rawdata.user_settlement_ratio
        }
    }
}