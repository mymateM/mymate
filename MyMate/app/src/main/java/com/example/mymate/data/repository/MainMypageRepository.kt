package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.RetrofitClientInstance
import com.example.mymate.data.dto.setting.UserInfo
import com.example.mymate.myPageApi

class MainMypageRepository(private val userRepo: DataStoreRepoUser) {
    suspend fun getUserInfo(): UserInfo {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(myPageApi::class.java)
        var userInfo = UserInfo()
        try {
            val result = endpoint!!.myPageApi("Bearer $accessToken")
            userInfo = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return userInfo
    }

}