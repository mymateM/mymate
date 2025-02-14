package com.example.mymate

import com.example.mymate.data.dto.expense.response.HomeInfoResponse

class MainHomeRepository(private val userRepo: DataStoreRepoUser) {
    private var homeInfo: HomeInfoResponse = HomeInfoResponse()

    private fun getAccessToken(): String {
        return userRepo.userAccessReadFlow.toString()
    }

    suspend fun getHomeInfo(): HomeInfoResponse {
        val accessToken = getAccessToken()
        val endpoint = RetrofitClientInstance.client?.create(getHomeInfo::class.java)
        try {
            val result = endpoint!!.getHomeInfo("Bearer $accessToken")
            homeInfo = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return homeInfo
    }
}