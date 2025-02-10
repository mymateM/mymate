package com.example.mymate.data.dto.report

data class UserMonthlyResult (
    var household_expense_total: String = "",
    var settlement_date: ReportDate = ReportDate(),
    var user: UserMonthlyStatus = UserMonthlyStatus()
)