package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.remote.service.RetrofitClientInstance
import com.example.mymate.data.dto.setting.UserInfo
import com.example.mymate.data.remote.service.setting.SettingApi

class MainMypageRepository(private val userRepo: DataStoreRepoUser) {
    private val endPoint = RetrofitClientInstance.client?.create(SettingApi::class.java)
    suspend fun getUserInfo(): UserInfo {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var userInfo = UserInfo()
        try {
            val result = endPoint!!.myPageApi("Bearer $accessToken")
            userInfo = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return userInfo
    }

}