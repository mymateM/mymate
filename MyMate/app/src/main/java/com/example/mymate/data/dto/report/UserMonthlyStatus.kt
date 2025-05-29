package com.example.mymate.data.dto.report

data class UserMonthlyStatus (
    var id: String = "",
    var name: String = "",
    var real_expense: String = "0",
    var ratio_expense: String = "0",
    var is_settlement_sender: Boolean = false,
    var settlement_amount: String = "0"
)