package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.remote.service.RetrofitClientInstance
import com.example.mymate.data.dto.report.HouseholdReportData
import com.example.mymate.data.dto.report.UserReportData
import com.example.mymate.data.remote.service.report.ReportApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainReportRepository(private val userRepo: DataStoreRepoUser) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val endPoint = RetrofitClientInstance.client?.create(ReportApi::class.java)

    suspend fun getPeriodDate(): String {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var period = ""
        try {
            val result = endPoint!!.getSettlementDate("Bearer $accessToken")
            period = result.body()!!.data.toString()
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return period
    }

    suspend fun getMyReport(month: Int, day: Int): UserReportData {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var reportData = UserReportData()
        val date = LocalDate.now().withMonth(month).withDayOfMonth(day).format(formatter)
        try {
            val result = endPoint!!.getMyReport("Bearer $accessToken", date)
            reportData = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return reportData
    }

    suspend fun getHouseholdReport(month: Int, day: Int): HouseholdReportData {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var reportData = HouseholdReportData()
        val date = LocalDate.now().withMonth(month).withDayOfMonth(day).format(formatter)
        try {
            val result = endPoint!!.getHouseholdReport("Bearer $accessToken", date)
            reportData = result.body()!!.data
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return reportData
    }
}