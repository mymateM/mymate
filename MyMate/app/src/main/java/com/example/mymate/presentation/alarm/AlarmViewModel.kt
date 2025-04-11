package com.example.mymate.presentation.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.dto.notification.UserActNotiDetail
import com.example.mymate.data.dto.notification.UserExpNotiDetail
import com.example.mymate.data.repository.AlarmRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.AlarmUseCase
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application): AndroidViewModel(application) {
    private val alarmUseCase = AlarmUseCase(AlarmRepository(DataStoreRepoUser(application.dataStore)))

    private val _isBadgeGone = MutableLiveData<Boolean>()
    val isBadgeGone: LiveData<Boolean> get() = _isBadgeGone

    private val _actNoti = MutableLiveData<ArrayList<ArrayList<UserActNotiDetail>>>()
    val actNoti: LiveData<ArrayList<ArrayList<UserActNotiDetail>>> get() = _actNoti

    private val _expNoti = MutableLiveData<ArrayList<ArrayList<UserExpNotiDetail>>>()
    val expNoti: LiveData<ArrayList<ArrayList<UserExpNotiDetail>>> get() = _expNoti

    init {
        checkExpNoti()
    }

    fun checkExpNoti() {
        viewModelScope.launch {
            _isBadgeGone.value = alarmUseCase.isExpenseNotiNew()
        }
    }

    fun getActNoti() {
        viewModelScope.launch {
            _actNoti.value = alarmUseCase.getActNotiByDate(alarmUseCase.getActivityNotiInfo())
        }
    }

    fun getExpNoti() {
        viewModelScope.launch {
            _expNoti.value = alarmUseCase.getExpNotiByDate(alarmUseCase.getExpenseNotiInfo())
        }
    }

    fun readExpNoti() {
        val id = _expNoti.value?.get(0)?.get(0)?.expense_notification_id
        if (id != null) {
            alarmUseCase.readExpNoti(id)
        }
    }
}