package com.example.mymate.data.remote.api.auth

import com.example.mymate.data.dto.auth.DeviceToken
import com.example.mymate.data.dto.auth.request.LocalLoginRequest
import com.example.mymate.data.dto.auth.request.SocialLoginRequest
import com.example.mymate.data.dto.auth.response.LocalLoginResponse
import com.example.mymate.data.dto.common.MemberIdResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("api/v1/auth/authenticate")
    fun localLogin(@Body req: LocalLoginRequest) : Call<LocalLoginResponse>

    @POST("api/v1/auth/authenticate/social")
    fun socialLogin(@Body req: SocialLoginRequest) : Call<LocalLoginResponse>

    @POST("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String, @Body req: DeviceToken) : Call<Response<Void>>

    @GET("api/v1/household/member/ids")
    fun getMemberId(@Header("Authorization") Authorization: String) : Call<MemberIdResponse>
}