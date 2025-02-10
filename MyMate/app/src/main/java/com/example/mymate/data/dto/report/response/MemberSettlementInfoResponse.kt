package com.example.mymate.data.dto.report.response

import com.example.mymate.data.dto.report.MemberMonthlyResult

data class MemberSettlementInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: MemberMonthlyResult = MemberMonthlyResult()
)