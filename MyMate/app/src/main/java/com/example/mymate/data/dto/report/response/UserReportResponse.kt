package com.example.mymate.data.dto.report.response

import com.example.mymate.data.dto.report.UserReportData

data class UserReportResponse (
    var message: String = "",
    var status: String = "",
    var data: UserReportData = UserReportData()
)