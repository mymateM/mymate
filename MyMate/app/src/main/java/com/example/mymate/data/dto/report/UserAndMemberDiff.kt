package com.example.mymate.data.dto.report

data class UserAndMemberDiff (
    var id: String = "",
    var name: String = "",
    var is_settlement_sender: Boolean = false,
    var settlement_amount: String = ""
)