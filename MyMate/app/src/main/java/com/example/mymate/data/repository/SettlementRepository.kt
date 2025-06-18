package com.example.mymate.data.repository

import com.example.mymate.*
import com.example.mymate.data.dto.report.HouseholdReportData
import com.example.mymate.data.dto.report.MemberMonthlyResult
import com.example.mymate.data.dto.report.UserMonthlyResult
import com.example.mymate.data.dto.report.UserReportData
import com.example.mymate.data.remote.service.report.ReportApi
import com.example.mymate.data.remote.service.RetrofitClientInstance
import com.example.mymate.data.remote.service.report.SettlementApi
import java.time.format.DateTimeFormatter

class SettlementRepository(private val userRepo: DataStoreRepoUser) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //TODO: 비슷한 형식의 formatter가 자주 사용됨. Util로 빼는 택지 고려할 것.
    private val reportEndPoint = RetrofitClientInstance.client?.create(ReportApi::class.java)
    private val settleEndPoint = RetrofitClientInstance.client?.create(SettlementApi::class.java)

    fun getSettlementDate(): String { //TODO: 같은 함수가 MainReportRepository에서도 사용되고 있음. Util로 빼는 선택지도 고려할 것
        val accessToken = userRepo.userAccessReadFlow.toString()
        var period = ""
        try {
            val result = reportEndPoint!!.getSettlementDate("Bearer $accessToken")
            period = result.body()!!.data.toString()
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return period
    }

    fun getMySettleInfo(startDate: String, endDate: String): UserMonthlyResult {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var monthlyResult = UserMonthlyResult()
        try {
            val result = settleEndPoint!!.getMySettleInfo("Bearer $accessToken", startDate, endDate)
            monthlyResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return monthlyResult
    }

    fun getMateSettleInfo(startDate: String, endDate: String): MemberMonthlyResult {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var monthlyResult = MemberMonthlyResult()
        try {
            val result = settleEndPoint!!.getMateSettleInfo("Bearer $accessToken", startDate, endDate)
            monthlyResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return monthlyResult
    }

    fun getHouseholdReport(date: String): HouseholdReportData { //TODO: 비슷한 함수가 MainReportRepository에서도 사용되고 있음.
        val accessToken = userRepo.userAccessReadFlow.toString()
        var houseResult = HouseholdReportData()
        try {
            val result = reportEndPoint!!.getHouseholdReport("Bearer $accessToken", date)
            houseResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return houseResult
    }

    fun getMyReport(date: String): UserReportData {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var myResult = UserReportData()
        try {
            val result = reportEndPoint!!.getMyReport("Bearer $accessToken", date)
            myResult = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return myResult
    }

    fun sendMoneyRequest(id: String) {
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = settleEndPoint!!.sendMoneyRequest("Bearer $accessToken", id)
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
    }
}