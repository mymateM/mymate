package com.example.mymate.data.remote.service.home

import com.example.mymate.data.dto.expense.response.HomeInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApi {
    @GET("api/v1/household/home")
    suspend fun getHomeInfo(@Header("Authorization") Authorization: String): Response<HomeInfoResponse>


}