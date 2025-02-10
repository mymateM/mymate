package com.example.mymate.data.dto.report.response

import com.example.mymate.data.dto.report.HouseholdReportData

data class HouseholdReportResponse (
    var message: String = "",
    var status: String = "",
    var data: HouseholdReportData = HouseholdReportData()
)