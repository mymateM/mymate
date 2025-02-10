package com.example.mymate.data.dto.report.response

import com.example.mymate.data.dto.report.UserMonthlyResult

data class UserSettlementInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: UserMonthlyResult = UserMonthlyResult()
)