package com.example.mymate

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

//User API

// data class

data class localUser (
    var user_email: String = "",
    var user_password: String = ""
)

data class loginUser (
    var email: String = "",
    var password: String = ""
)

data class localUserRegister (
    var nickname: String = "",
    var email: String = "",
    var password: String = ""
)

data class token (
    var access_token: String = "",
    var refresh_token: String = ""
)

data class devicetoken (
    var deviceToken: String = ""
)

// data class for responses

data class defaultResponse (
    var data: String = "",
    var links: String = ""
)

data class localLoginResponse (
    var message: String = "",
    var status: String = "",
    var data: token = token()
)

data class localRefreshReponse (
    var access_token: String = "",
    var refresh_token: String = ""
)

interface localLogin {
    @POST("api/v1/auth/authenticate")
    fun localLogin(@Body req: loginUser) : Call<localLoginResponse>
}

interface localRefresh {
    @GET("api/v1/auth/reissue")
    fun localRefresh(@Header("Authorization") Authorization: String) : Call<localRefreshReponse>
}

interface localRegister {
    @POST("api/v1/auth/register")
    fun localRegister(@Body req: localUserRegister) : Call<localLoginResponse>
}

interface localDevice {
    @POST("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String, @Body req: devicetoken)
}

//이하 완료되지 않은 API interface

//Household API

//Settlement API

//Alarm API

//Bills Api

//Expense API

//Reports API