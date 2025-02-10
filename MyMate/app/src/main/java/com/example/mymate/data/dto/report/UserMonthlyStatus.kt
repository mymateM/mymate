package com.example.mymate.data.dto.report

data class UserMonthlyStatus (
    var id: String = "",
    var name: String = "",
    var real_expense: String = "",
    var ratio_expense: String = "",
    var is_settlement_sender: Boolean = false,
    var settlement_amount: String = ""
)