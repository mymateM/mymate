package com.example.mymate.data.repository

import com.example.mymate.*
import com.example.mymate.data.dto.notification.UserActNoti
import com.example.mymate.data.dto.notification.UserExpNoti
import com.example.mymate.data.remote.service.notification.NotificationApi
import com.example.mymate.data.remote.service.RetrofitClientInstance

class AlarmRepository(private val userRepo: DataStoreRepoUser) {
    private val endPoint = RetrofitClientInstance.client?.create(NotificationApi::class.java)
    fun getActivityNoti(): UserActNoti {
        var actNoti = UserActNoti()
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = endPoint!!.getActivityNoti("Bearer $accessToken")
            actNoti = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return actNoti
    }

    fun getExpenseNoti(): UserExpNoti {
        var expNoti = UserExpNoti()
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = endPoint!!.getExpenseNoti("Bearer $accessToken")
            expNoti = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return expNoti
    }

    fun readActivityNoti(id: String): Boolean  {
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = endPoint!!.readActivityNoti("Bearer $accessToken", id)
            return result.isSuccessful
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
            return false
        }
    }

    fun readExpenseNoti(id: String): Boolean {
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = endPoint!!.readExpenseNoti("Bearer $accessToken", id)
            return result.isSuccessful
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
            return false
        }
    }
}