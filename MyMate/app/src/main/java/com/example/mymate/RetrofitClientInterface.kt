package com.example.mymate

import android.icu.text.CaseMap.Title
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

//User API

// data class

data class error (
    var field: String = "",
    var value: String = "",
    var reason: String = ""
)

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

data class socialUserLogin (
    var socialAuthType: String = "",
    var socialAccessToken: String = ""
)

data class token (
    var access_token: String = "",
    var refresh_token: String = ""
)

data class devicetoken (
    var deviceToken: String = ""
)

data class senddeviceToken (
    var device_token: String = ""
)

data class dailyExpenseDetail (
    var settlement_date: datePeriod = datePeriod(),
    var household_budget_amount: String = "",
    var household_by_now_expense: String = "",
    var household_expenses: ArrayList<householdExpenseDetail> = ArrayList()
)

data class datePeriod (
    var date_start: String = "",
    var date_end: String = ""
)

data class householdExpenseDetail (
    var expense_id: String = "",
    var expense_date: String = "",
    var expense_day_of_week: String = "",
    var expense_category_name: String = "",
    var expense_category_image_url: String = "",
    var expense_store: String = "",
    var expense_consumer: String = ""
)

data class activityNoti (
    var activity_notification_id: String = "",
    var category_title: String = "",
    var category_image_url: String = "",
    var is_read: Boolean = false,
    var created_at: String = "",
    var trigger: String? = ""
)

data class notiActivityData (
    var activityNotificationResponses: ArrayList<activityNoti> = ArrayList()
)

data class notiExpenseData (
    var notification_expenses: ArrayList<expenseNoti> = ArrayList()
)

data class expenseNoti (
    var expense_notification_id: String = "",
    var expense_category_image_url: String = "",
    var created_at: String = "",
    var is_read: Boolean = false,
    var expense_amount: String = "",
    var spender_name: String = ""
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

data class localRegisterResponse (
    var message: String = "",
    var status: String = "",
    var code: String = "",
    var data: token = token(),
    var errors: ArrayList<error> = ArrayList()
)

data class localRefreshReponse (
    var access_token: String = "",
    var refresh_token: String = ""
)

data class deviceTokenResponse (
    var message: String = "",
    var status: String = "",
    var data: senddeviceToken = senddeviceToken()
)

data class dailyExpenseResponse (
    var status: String = "",
    var title: String = "",
    var detail: String = "",
    var data: dailyExpenseDetail = dailyExpenseDetail(),
    var links: String = ""
)

data class activityNotiResponse (
    var message: String = "",
    var status: String = "",
    var data: notiActivityData = notiActivityData()
)

data class expenseNotiResponse (
    var message: String = "",
    var status: String = "",
    var data: notiExpenseData = notiExpenseData()
)

//login + token interface

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
    fun localRegister(@Body req: loginUser) : Call<localRegisterResponse>
}

interface socialLogin {
    @POST("api/v1/auth/authenticate/social")
    fun soicalLogin(@Body req: socialUserLogin) : Call<localLoginResponse>
}

//TODO: local register error API response error receiving (1: DataClass, 2: receiving check)

interface localDevice {
    @POST("api/v1/auth/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String, @Body req: devicetoken) : Call<Response<Void>>
}

interface getlocalDevice {
    @GET("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String) : Call<deviceTokenResponse>
}

//이하 완료되지 않은 API interface

//Household API

//Settlement API

//Alarm API

interface getActivityNoti {
    @GET("api/v1/notifications/activity")
    fun activityNoti(@Header("Authorization") Authorization: String) : Call<activityNotiResponse>
}

interface getExpenseNoti {
    @GET("api/v1/notifications/expense")
    fun expenseNoti(@Header("Authorization") Authorization: String) : Call<expenseNotiResponse>
}

//Bills Api

//Expense API

interface getDailyExpense {
    @GET("expenses/daily-content")
    fun getDailyExpense(@Header("Authentication") Authentication: String) : Call<dailyExpenseResponse>
}

//Reports API