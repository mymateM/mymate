package com.example.mymate.data.remote.api.report

import com.example.mymate.data.dto.report.response.MemberSettlementInfoResponse
import com.example.mymate.data.dto.report.response.UserSettlementInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SettlementApi {
    @GET("api/v1/settlement/user")
    fun getMySettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Response<UserSettlementInfoResponse>

    @GET("api/v1/settlement")
    fun getMateSettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Response<MemberSettlementInfoResponse>

    @GET("api/v1/notifications/send-money/{user_id}")
    fun sendMoneyRequest(@Header("Authorization") Authorization: String, @Path("user_id") user_id: String): Response<Void>
}