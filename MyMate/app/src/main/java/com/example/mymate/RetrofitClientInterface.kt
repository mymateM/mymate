package com.example.mymate

import com.example.mymate.data.dto.auth.*
import com.example.mymate.data.dto.auth.request.*
import com.example.mymate.data.dto.common.*
import com.example.mymate.data.dto.auth.response.*
import com.example.mymate.data.dto.notification.response.*
import com.example.mymate.data.dto.bill.response.*
import com.example.mymate.data.dto.bill.request.*
import com.example.mymate.data.dto.setting.response.*
import com.example.mymate.data.dto.setting.request.*
import com.example.mymate.data.dto.report.response.*
import com.example.mymate.data.dto.expense.request.*
import com.example.mymate.data.dto.expense.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface localLogin {
    @POST("api/v1/auth/authenticate")
    fun localLogin(@Body req: LocalLoginRequest) : Call<LocalLoginResponse>
}

interface socialLogin {
    @POST("api/v1/auth/authenticate/social")
    fun socialLogin(@Body req: SocialLoginRequest) : Call<LocalLoginResponse>
}

interface localDevice {
    @POST("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String, @Body req: DeviceToken) : Call<Response<Void>>
}

interface getMemberId {
    @GET("api/v1/household/member/ids")
    fun getMemberId(@Header("Authorization") Authorization: String) : Call<MemberIdResponse>
}

//Home API

interface getHomeInfo {
    @GET("api/v1/household/home")
    suspend fun getHomeInfo(@Header("Authorization") Authorization: String): Response<HomeInfoResponse>
}

//Settlement API

interface getMySettleInfo {
    @GET("api/v1/settlement/user")
    fun getMySettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<UserSettlementInfoResponse>
}

interface getMateSettleInfo {
    @GET("api/v1/settlement")
    fun getMateSettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<MemberSettlementInfoResponse>
}

interface sendMoneyRequest {
    @GET("api/v1/notifications/send-money/{user_id}")
    fun sendMoneyRequest(@Header("Authorization") Authorization: String, @Path("user_id") user_id: String): Call<Response<Void>>
}

//Alarm API

interface getActivityNoti {
    @GET("api/v1/notifications/activity")
    fun activityNoti(@Header("Authorization") Authorization: String) : Call<UserActNotiResponse>
}

interface getExpenseNoti {
    @GET("api/v1/notifications/expense")
    fun expenseNoti(@Header("Authorization") Authorization: String) : Call<UserExpNotiResponse>
}

interface readActivityNoti {
    @POST("api/v1/notifications/activity/is-read/true")
    fun readActivityNoti(@Header("Authorization") Authorization: String, @Query("activity_notification_ids") activity_notification_ids: String): Call<DefaultResponse>
}

interface readExpenseNoti {
    @POST("api/v1/notifications/expense/is-read/true")
    fun readExpenseNoti(@Header("Authorization") Authorization: String, @Query("expense_notification_ids") expense_notification_ids: String): Call<DefaultResponse>
}

//Calendar Api

interface getCalendar {
    @GET("api/v1/expense/daily-total/month/{year}/{month}/{day}")
    fun getCalendar(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("day") day: String) : Response<CalendarResponse>
}

//Bills Api

interface getGasBillList {
    @GET("api/v1/bills/GAS")
    fun getGasBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>
}

interface getElectricityBillList {
    @GET("api/v1/bills/ELECTRICITY")
    fun getElectricityBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>
}

interface getWaterBillList {
    @GET("api/v1/bills/WATER")
    fun getWaterBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>
}

interface getEtcBillList {
    @GET("api/v1/bills/ETC")
    fun getEtcBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>
}

interface getBill {
    @GET("api/v1/bill/{bill_id}")
    fun getBill(@Header("Authorization") Authorization: String, @Path("bill_id") bill_id: String) : Call<BillDetailResponse>
}

interface getBillCategory {
    @GET("api/v1/bills/category")
    fun getBillCategory(@Header("Authorization") Authorization: String): Call<BillCategoryResponse>
}

interface postBill {
    @POST("api/v1/bill")
    fun postBill(@Header("Authorization") Authorization: String, @Body req: BillWriteRequest): Call<DefaultResponse>
}

interface deleteBill {
    @DELETE("api/v1/bills")
    fun deleteBill(@Header("Authorization") Authorization: String, @Query("bill_id_list") bill_id_list: String): Call<DefaultResponse>
}

//Expense API

interface getDailyExpense {
    @GET("api/v1/expense/daily-total/day/{year}/{month}/{dayOfMonth}")
    fun getDailyExpense(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("dayOfMonth") dayOfMonth: String) : Response<DailyExpenseResponse>
}

interface putDailyExpense {
    @POST("api/v1/expense")
    fun putDailyExpense(@Header("Authorization") Authorization: String, @Body req: ExpenseWriteRequest): Call<DefaultResponse>
}

interface getDailySingleExpense {
    @GET("api/v1/expense/{expense_id}")
    fun getDailySingleExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<DailySingleExpenseResponse>
}

interface searchExpense {
    @POST("api/v1/expense/search")
    fun searchExpense(@Header("Authorization") Authorization: String, @Query("expense_date_max") expense_date_max: String, @Query("expense_date_min") expense_date_min: String, @Query("expense_category_name") expense_category_name: String, @Query("expense_amount_max") expense_amount_max: String, @Query("expense_amount_min") expense_amount_min: String, @Query("sorted_by_newest") sorted_by_newest: Boolean): Call<SearchResponse>
}

interface deleteExpense {
    @DELETE("api/v1/expense/{expense_id}")
    fun deleteExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<Response<Void>>
}

//Reports API

interface getSettlementDate {
    @GET("api/v1/household/settlement/date")
    fun getSettlementDate(@Header("Authorization") Authorization: String): Call<DefaultResponse>
}

interface getHouseholdReport {
    @GET("api/v1/report/household/{report-start-date}")
    fun getHouseholdReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Call<HouseholdReportResponse>
}

interface getMyReport {
    @GET("api/v1/report/user/{report-start-date}")
    fun getMyReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Call<UserReportResponse>
}

//Mypage API

interface myPageApi {
    @GET("api/v1/mypage")
    fun myPageApi(@Header("Authorization") Authorization: String): Call<UserInfoResponse>
}

interface getHouseRatio {
    @GET("api/v1/household/members/settlement-ratio")
    fun getHouseRatio(@Header("Authorization") Authorization: String): Call<HouseSettlementRatioResponse>
}

interface getMyAccount {
    @GET("api/v1/user/account")
    fun getMyAccount(@Header("Authorization") Authorization: String): Call<UserAccountResponse>
}

interface postSettleDay {
    @POST("api/v1/household/settlement-date")
    fun postSettleDay(@Header("Authorization") Authorization: String, @Body req: SettlementDayRequest): Call<Response<Void>>
}

interface getMyBudget {
    @GET("api/v1/household/budget")
    fun getMyBudget(@Header("Authorization") Authorization: String): Call<UserBudgetResponse>
}

interface postHouseRatio {
    @POST("api/v1/roomates/settlement-ratio")
    fun postHouseRatio(@Header("Authorization") Authorization: String, @Body req: HouseMemberRatioRequest): Call<Response<Void>>
}

interface postMyAccount {
    @POST("api/v1/user/account")
    fun postMyAccount(@Header("Authorization") Authorization: String, @Body req: UserAccountRequest): Call<Response<Void>>
}