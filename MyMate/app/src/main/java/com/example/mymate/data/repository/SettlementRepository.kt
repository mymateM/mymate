package com.example.mymate.data.repository

import androidx.compose.runtime.rememberUpdatedState
import com.example.mymate.*
import com.example.mymate.data.dto.report.HouseholdReportData
import com.example.mymate.data.dto.report.MemberMonthlyResult
import com.example.mymate.data.dto.report.UserMonthlyResult
import com.example.mymate.data.dto.report.UserReportData
import com.example.mymate.data.dto.report.response.UserReportResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SettlementRepository(private val userRepo: DataStoreRepoUser) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //TODO: 비슷한 형식의 formatter가 자주 사용됨. Util로 빼는 택지 고려할 것.

    fun getSettlementDate(): String { //TODO: 같은 함수가 MainReportRepository에서도 사용되고 있음. Util로 빼는 선택지도 고려할 것
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getSettlementDate::class.java)
        var period = ""
        try {
            val result = endpoint!!.getSettlementDate("Bearer $accessToken")
            period = result.body()!!.data.toString()
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return period
    }

    fun getMySettleInfo(startDate: String, endDate: String): UserMonthlyResult {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getMySettleInfo::class.java)
        var monthlyResult = UserMonthlyResult()
        try {
            val result = endpoint!!.getMySettleInfo("Bearer $accessToken", startDate, endDate)
            monthlyResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return monthlyResult
    }

    fun getMateSettleInfo(startDate: String, endDate: String): MemberMonthlyResult {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getMateSettleInfo::class.java)
        var monthlyResult = MemberMonthlyResult()
        try {
            val result = endpoint!!.getMateSettleInfo("Bearer $accessToken", startDate, endDate)
            monthlyResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return monthlyResult
    }

    fun getHouseholdReport(date: String): HouseholdReportData { //TODO: 비슷한 함수가 MainReportRepository에서도 사용되고 있음.
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getHouseholdReport::class.java)
        var houseResult = HouseholdReportData()
        try {
            val result = endpoint!!.getHouseholdReport("Bearer $accessToken", date)
            houseResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return houseResult
    }

    fun getMyReport(date: String): UserReportData {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getMyReport::class.java)
        var myResult = UserReportData()
        try {
            val result = endpoint!!.getMyReport("Bearer $accessToken", date)
            myResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return myResult
    }

    fun sendMoneyRequest(id: String) {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(sendMoneyRequest::class.java)
        try {
            val result = endpoint!!.sendMoneyRequest("Bearer $accessToken", id)
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
    }
}