package com.example.mymate.data.remote.api.report

import com.example.mymate.data.dto.common.DefaultResponse
import com.example.mymate.data.dto.report.response.HouseholdReportResponse
import com.example.mymate.data.dto.report.response.UserReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {
    @GET("api/v1/household/settlement/date")
    fun getSettlementDate(@Header("Authorization") Authorization: String): Response<DefaultResponse>

    @GET("api/v1/report/household/{report-start-date}")
    fun getHouseholdReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Response<HouseholdReportResponse>

    @GET("api/v1/report/user/{report-start-date}")
    fun getMyReport(@Header("Authorization") Authorization: String, @Path("report-start-date") report_start_date: String): Response<UserReportResponse>
}