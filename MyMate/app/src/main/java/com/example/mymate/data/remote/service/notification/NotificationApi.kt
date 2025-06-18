package com.example.mymate.data.remote.service.notification

import com.example.mymate.data.dto.common.DefaultResponse
import com.example.mymate.data.dto.notification.response.UserActNotiResponse
import com.example.mymate.data.dto.notification.response.UserExpNotiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationApi {
    @GET("api/v1/notifications/activity")
    fun getActivityNoti(@Header("Authorization") Authorization: String) : Response<UserActNotiResponse>

    @GET("api/v1/notifications/expense")
    fun getExpenseNoti(@Header("Authorization") Authorization: String) : Response<UserExpNotiResponse>

    @POST("api/v1/notifications/activity/is-read/true")
    fun readActivityNoti(@Header("Authorization") Authorization: String, @Query("activity_notification_ids") activity_notification_ids: String): Response<DefaultResponse>

    @POST("api/v1/notifications/expense/is-read/true")
    fun readExpenseNoti(@Header("Authorization") Authorization: String, @Query("expense_notification_ids") expense_notification_ids: String): Response<DefaultResponse>
}