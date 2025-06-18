package com.example.mymate.data.remote.api.setting

import com.example.mymate.data.dto.setting.request.HouseMemberRatioRequest
import com.example.mymate.data.dto.setting.request.SettlementDayRequest
import com.example.mymate.data.dto.setting.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SettingApi {
    @GET("api/v1/mypage")
    fun myPageApi(@Header("Authorization") Authorization: String): Response<UserInfoResponse>

    @GET("api/v1/household/members/settlement-ratio")
    fun getHouseRatio(@Header("Authorization") Authorization: String): Call<HouseSettlementRatioResponse>

    @GET("api/v1/user/account")
    fun getMyAccount(@Header("Authorization") Authorization: String): Call<UserAccountResponse>

    @POST("api/v1/household/settlement-date")
    fun postSettleDay(@Header("Authorization") Authorization: String, @Body req: SettlementDayRequest): Call<Response<Void>>

    @GET("api/v1/household/budget")
    fun getMyBudget(@Header("Authorization") Authorization: String): Call<UserBudgetResponse>

    @POST("api/v1/roomates/settlement-ratio")
    fun postHouseRatio(@Header("Authorization") Authorization: String, @Body req: HouseMemberRatioRequest): Call<Response<Void>>

    @POST("api/v1/user/account")
    fun postMyAccount(@Header("Authorization") Authorization: String, @Body req: UserAccountRequest): Call<Response<Void>>
}