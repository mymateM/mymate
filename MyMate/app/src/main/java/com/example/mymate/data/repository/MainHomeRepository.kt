package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.remote.service.RetrofitClientInstance
import com.example.mymate.data.dto.expense.response.HomeInfoResponse
import com.example.mymate.data.dto.notification.UserExpNoti
import com.example.mymate.data.remote.service.home.HomeApi
import com.example.mymate.data.remote.service.notification.NotificationApi

class MainHomeRepository(private val userRepo: DataStoreRepoUser) {
    private var homeInfo: HomeInfoResponse = HomeInfoResponse()
    private val homeEndPoint = RetrofitClientInstance.client?.create(HomeApi::class.java)
    private val notiEndPoint = RetrofitClientInstance.client?.create(NotificationApi::class.java)

    suspend fun getHomeInfo(): HomeInfoResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = homeEndPoint!!.getHomeInfo("Bearer $accessToken")
            homeInfo = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return homeInfo
    }

    fun getExpenseNoti(): UserExpNoti {
        var expNoti = UserExpNoti()
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = notiEndPoint!!.getExpenseNoti("Bearer $accessToken")
            expNoti = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return expNoti
    }
}