package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.RetrofitClientInstance
import com.example.mymate.data.dto.expense.response.HomeInfoResponse
import com.example.mymate.data.dto.notification.UserExpNoti

class MainHomeRepository(private val userRepo: DataStoreRepoUser) {
    private var homeInfo: HomeInfoResponse = HomeInfoResponse()

    suspend fun getHomeInfo(): HomeInfoResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(com.example.mymate.getHomeInfo::class.java)
        try {
            val result = endpoint!!.getHomeInfo("Bearer $accessToken")
            homeInfo = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return homeInfo
    }

    fun getExpenseNoti(): UserExpNoti {
        var expNoti = UserExpNoti()
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endPoint = RetrofitClientInstance.client?.create(com.example.mymate.getExpenseNoti::class.java)
        try {
            val result = endPoint!!.getExpenseNoti("Bearer $accessToken")
            expNoti = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return expNoti
    }
}