package com.example.mymate

import android.icu.text.CaseMap.Title
import androidx.camera.camera2.internal.compat.quirk.StillCaptureFlashStopRepeatingQuirk
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
    var expenses: ArrayList<ExpenseList> = ArrayList()
)

data class ExpenseList (
    var expenseId: String = "",
    var expenseAmount: String = "",
    var expenseStore: String = "",
    var expenseCategoryName: String = "",
    var expenseCategoryImage: String = "",
    var settlementSubjects: ArrayList<Expenses> = ArrayList(),
    var expenseDate: String = ""
)

data class Expenses (
    var userId: String = "",
    var userName: String = "",
    var isExpenseConsumer: Boolean = false,
    var userProfileImage: String = ""
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

data class bills (
    var bills: ArrayList<bill> = ArrayList()
)

data class bill (
    var bill_id: String = "",
    var bill_image_url: String = "",
    var bill_payment_date: String = "",
    var bill_store: String = "",
    var bill_payment_amount: String = ""
)

data class billdetail (
    var bill_category: String = "",
    var bill_payment_amount: String = "",
    var bill_image_url: String = "",
    var bill_payment_date: String = "",
    var bill_memo: String? = "",
    var register_date: String = ""
)

data class billcategoryList (
    var recent_bill_category: ArrayList<billcategory> = ArrayList()
)

data class billcategory (
    var bill_category: String = "",
    var bill_payment_date: String = "",
    var bill_payment_amount: String = ""
)

data class billtosend (
    var bill_payment_date: String = "",
    var bill_image: String = "",
    var bill_payment_amount: String = "",
    var bill_store: String = "",
    var bill_category_title: String = "",
    var bill_memo: String = "",
    var virtual_accounts: ArrayList<virtualAccounts> = ArrayList()
)

data class virtualAccounts (
    var bank_name: String = "",
    var account_number: String = ""
)

data class expensetosend (
    var expenseDate: String = "",
    var expenseAmount: String = "",
    var settlementSubjectIds: ArrayList<String> = ArrayList(),
    var expenseStore: String = "",
    var expenseCategory: String = "",
    var expenseMemo: String = ""
)

data class expenseSingle (
    var payment_amount: String = "",
    var expense_memo: String = "",
    var expense_category: String = "",
    var expense_store: String = "",
    var expense_register_date: String = ""
)

data class calendarList (
    var household_daily_expenses: ArrayList<calendarListitem> = ArrayList()
)

data class calendarListitem (
    var expense_date: String = "",
    var daily_total_expense: String = ""
)

data class myPageData (
    var user_image_url: String = "",
    var user_nickname: String = "",
    var user_settlement_ratio: String = "",
    var household_settlement_date: String = "",
    var household_budget_amount: String = ""
)

data class houseRatioList (
    var household_members: ArrayList<houseMembers> = ArrayList()
)

data class houseRatioPost (
    var household_members: ArrayList<houseMemberSimple> = ArrayList()
)

data class houseMemberSimple (
    var user_id: String = "",
    var user_settlement_ratio: String = ""
)

data class houseMembers (
    var user_id: String = "",
    var user_profile_image: String = "",
    var user_nickname: String = "",
    var user_settlement_ratio: String = ""
)

data class myAccount (
    var account_bank: String = "",
    var account_number: String = "",
    var account_image_url: String = "",
    var members: ArrayList<mateAccount> = ArrayList()
)

data class postAccount (
    var account_bank: String = "",
    var account_number: String = ""
)

data class homeHousehold (
    var house_id: String = "",
    var household_name: String = "",
    var by_now_expense: String = "",
    var by_now_budget_ratio: String = "",
    var settlement_d_day: String = "",
    var by_previous_expense: String = "",
    var now_expense_diff: String = "",
    var is_household_budget_over_warn: Boolean = false,
    var expense_duration: String = ""
)

data class homeMe (
    var user_id: String = "",
    var user_total_budget: String = "",
    var user_by_now_total_expense: String = "",
    var user_by_now_left_expense: String = ""
)

data class homeInfo (
    var household: homeHousehold = homeHousehold(),
    var me: homeMe = homeMe()
)

data class householdReportdata (
    var report_date: report_date = report_date(),
    var is_expense_over_budget: Boolean = false,
    var budget_real_expense_diff: String = "",
    var total_expense: String = "",
    var expense_categories: ArrayList<expense_category> = ArrayList()
)

data class myReportdata (
    var report_date: report_date = report_date(),
    var expense_categories: ArrayList<expense_category> = ArrayList()
)

data class report_date (
    var date_start: String = "",
    var date_end: String = ""
)

data class expense_category (
    var category_name: String = "",
    var category_img: String = "",
    var total_expense_ratio: String = "",
    var total_expense_amount: String = ""
)

data class mySettleInfoData (
    var household_expense_total: String = "",
    var settlement_date: report_date = report_date(),
    var user: mySettleInfo = mySettleInfo()
)

data class mySettleInfo (
    var id: String = "",
    var name: String = "",
    var real_expense: String = "",
    var ratio_expense: String = "",
    var is_settlement_sender: Boolean = false,
    var settlement_amount: String = ""
)

data class mySettleShortInfo (
    var id: String = "",
    var name: String = "",
    var is_settlement_sender: Boolean = false,
    var settlement_amount: String = ""
)

data class mateSettleInfoData (
    var settlement_date: report_date = report_date(),
    var user: mySettleShortInfo = mySettleShortInfo(),
    var roommates: ArrayList<mateSettleInfo> = ArrayList()
)

data class mateSettleInfo (
    var id: String = "",
    var name: String = "",
    var settlement_amount: String = "",
    var account_bank: String = "",
    var account_number: String = ""
)

data class mateAccount (
    var user_name: String = "",
    var user_profile_image: String = "",
    var account_bank: String = "",
    var account_number: String = ""
)

data class settledaytosend (
    var settlement_day_of_month: String = ""
)

data class budget (
    var budget_amount: String = "",
    var budget_allowance: String = ""
)

// data class for responses

data class defaultResponse (
    var message: String = "",
    var status: String = "",
    var data: String? = ""
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
    var message: String = "",
    var status: String = "",
    var data: dailyExpenseDetail = dailyExpenseDetail()
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

data class billListResponse (
    var message: String = "",
    var status: String = "",
    var data: bills = bills()
)

data class billDetailResponse (
    var message: String = "",
    var status: String = "",
    var data: billdetail = billdetail()
)

data class billCategoryResponse (
    var message: String = "",
    var status: String = "",
    var data: billcategoryList = billcategoryList()
)

data class postbillResponse (
    var message: String = "",
    var status: String = "",
    var data: String = ""
)

data class getMemberIdResponse (
    var message: String = "",
    var status: String = "",
    var data: ArrayList<String> = ArrayList()
)

data class getDailySingleExpenseResult (
    var message: String = "",
    var status: String = "",
    var data: expenseSingle = expenseSingle()
)

data class searchResponse (
    var message: String = "",
    var status: String = "",
    var data: dailyExpenseDetail = dailyExpenseDetail()
)

data class calendarResponse (
    var message: String = "",
    var status: String = "",
    var data: calendarList = calendarList()
)

data class myPageApiResponse (
    var message: String = "",
    var status: String = "",
    var data: myPageData = myPageData()
)

data class houseRatioResponse (
    var message: String = "",
    var status: String = "",
    var data: houseRatioList = houseRatioList()
)

data class myAccountResponse (
    var message: String = "",
    var status: String = "",
    var data: myAccount = myAccount()
)

data class homeInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: homeInfo = homeInfo()
)

data class settlementDateResponse (
    var message: String = "",
    var status: String = "",
    var data: String = ""
)

data class householdReportResponse (
    var message: String = "",
    var status: String = "",
    var data: householdReportdata = householdReportdata()
)

data class myReportResponse (
    var message: String = "",
    var status: String = "",
    var data: myReportdata = myReportdata()
)

data class mySettleInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: mySettleInfoData = mySettleInfoData()
)

data class mateSettleInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: mateSettleInfoData = mateSettleInfoData()
)

data class myBudgetResponse (
    var message: String = "",
    var status: String = "",
    var data: budget = budget()
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
    @POST("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String, @Body req: devicetoken) : Call<Response<Void>>
}

interface getlocalDevice {
    @GET("api/v1/user/device-token")
    fun localDevice(@Header("Authorization") Authorization: String) : Call<deviceTokenResponse>
}

interface getMemberId {
    @GET("api/v1/household/member/ids")
    fun getMemberId(@Header("Authorization") Authorization: String) : Call<getMemberIdResponse>
}

//Home API

interface getHomeInfo {
    @GET("api/v1/household/home")
    fun getHomeInfo(@Header("Authorization") Authorization: String): Call<homeInfoResponse>
}

//Settlement API

interface getMySettleInfo {
    @GET("api/v1/settlement/user")
    fun getMySettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<mySettleInfoResponse>
}

interface getMateSettleInfo {
    @GET("api/v1/settlement")
    fun getMateSettleInfo(@Header("Authorization") Authorization: String, @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<mateSettleInfoResponse>
}

interface sendMoneyRequest {
    @GET("api/v1/notifications/send-money/{user_id}")
    fun sendMoneyRequest(@Header("Authorization") Authorization: String, @Path("user_id") user_id: String): Call<Response<Void>>
}

//Alarm API

interface getActivityNoti {
    @GET("api/v1/notifications/activity")
    fun activityNoti(@Header("Authorization") Authorization: String) : Call<activityNotiResponse>
}

interface getExpenseNoti {
    @GET("api/v1/notifications/expense")
    fun expenseNoti(@Header("Authorization") Authorization: String) : Call<expenseNotiResponse>
}

interface readActivityNoti {
    @POST("api/v1/notifications/activity/is-read/true")
    fun readActivityNoti(@Header("Authorization") Authorization: String, @Query("activity_notification_ids") activity_notification_ids: String): Call<defaultResponse>
}

interface readExpenseNoti {
    @POST("api/v1/notifications/expense/is-read/true")
    fun readExpenseNoti(@Header("Authorization") Authorization: String, @Query("expense_notification_ids") expense_notification_ids: String): Call<defaultResponse>
}

//Calendar Api

interface getCalendar {
    @GET("api/v1/expense/daily-total/month/{year}/{month}/{day}")
    fun getCalendar(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("day") day: String) : Call<calendarResponse>
}

//Bills Api

interface getGasBillList {
    @GET("api/v1/bills/GAS")
    fun getGasBillList(@Header("Authorization") Authorization: String) : Call<billListResponse>
}

interface getElectricityBillList {
    @GET("api/v1/bills/ELECTRICITY")
    fun getElectricityBillList(@Header("Authorization") Authorization: String) : Call<billListResponse>
}

interface getWaterBillList {
    @GET("api/v1/bills/WATER")
    fun getWaterBillList(@Header("Authorization") Authorization: String) : Call<billListResponse>
}

interface getEtcBillList {
    @GET("api/v1/bills/ETC")
    fun getEtcBillList(@Header("Authorization") Authorization: String) : Call<billListResponse>
}

interface getBill {
    @GET("api/v1/bill/{bill_id}")
    fun getBill(@Header("Authorization") Authorization: String, @Path("bill_id") bill_id: String) : Call<billDetailResponse>
}

interface getBillCategory {
    @GET("api/v1/bills/category")
    fun getBillCategory(@Header("Authorization") Authorization: String): Call<billCategoryResponse>
}

interface postBill {
    @POST("api/v1/bill")
    fun postBill(@Header("Authorization") Authorization: String, @Body req: billtosend): Call<postbillResponse>
}

interface deleteBill {
    @DELETE("api/v1/bills")
    fun deleteBill(@Header("Authorization") Authorization: String, @Query("bill_id_list") bill_id_list: String): Call<postbillResponse>
}

//Expense API

interface getDailyExpense {
    @GET("api/v1/expense/daily-total/day/{year}/{month}/{dayOfMonth}")
    fun getDailyExpense(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("dayOfMonth") dayOfMonth: String) : Call<dailyExpenseResponse>
}

interface putDailyExpense {
    @POST("api/v1/expense")
    fun putDailyExpense(@Header("Authorization") Authorization: String, @Body req: expensetosend): Call<postbillResponse>
}

interface getDailySingleExpense {
    @GET("api/v1/expense/{expense_id}")
    fun getDailySingleExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<getDailySingleExpenseResult>
}

interface searchExpense {
    @POST("api/v1/expense/search")
    fun searchExpense(@Header("Authorization") Authorization: String, @Query("expense_date_max") expense_date_max: String, @Query("expense_date_min") expense_date_min: String, @Query("expense_category_name") expense_category_name: String, @Query("expense_amount_max") expense_amount_max: String, @Query("expense_amount_min") expense_amount_min: String, @Query("sorted_by_newest") sorted_by_newest: Boolean): Call<searchResponse>
}

interface deleteExpense {
    @DELETE("api/v1/expense/{expense_id}")
    fun deleteExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<Response<Void>>
}

//Reports API

interface getSettlementDate {
    @GET("api/v1/household/settlement/date")
    fun getSettlementDate(@Header("Authorization") Authorization: String): Call<settlementDateResponse>
}

interface getHouseholdReport {
    @GET("api/v1/report/household/{report-start-date}")
    fun getHouseholdReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Call<householdReportResponse>
}

interface getMyReport {
    @GET("api/v1/report/user/{report-start-date}")
    fun getMyReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Call<myReportResponse>
}

//Mypage API

interface myPageApi {
    @GET("api/v1/mypage")
    fun myPageApi(@Header("Authorization") Authorization: String): Call<myPageApiResponse>
}

interface getHouseRatio {
    @GET("api/v1/household/members/settlement-ratio")
    fun getHouseRatio(@Header("Authorization") Authorization: String): Call<houseRatioResponse>
}

interface getMyAccount {
    @GET("api/v1/user/account")
    fun getMyAccount(@Header("Authorization") Authorization: String): Call<myAccountResponse>
}

interface postSettleDay {
    @POST("api/v1/household/settlement-date")
    fun postSettleDay(@Header("Authorization") Authorization: String, @Body req: settledaytosend): Call<Response<Void>>
}

interface getMyBudget {
    @GET("api/v1/household/budget")
    fun getMyBudget(@Header("Authorization") Authorization: String): Call<myBudgetResponse>
}

interface postHouseRatio {
    @POST("api/v1/roomates/settlement-ratio")
    fun postHouseRatio(@Header("Authorization") Authorization: String, @Body req: houseRatioPost): Call<Response<Void>>
}

interface postMyAccount {
    @POST("api/v1/user/account")
    fun postMyAccount(@Header("Authorization") Authorization: String, @Body req: postAccount): Call<Response<Void>>
}