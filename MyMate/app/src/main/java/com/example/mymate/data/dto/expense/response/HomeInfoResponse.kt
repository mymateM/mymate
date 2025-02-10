package com.example.mymate.data.dto.expense.response

import com.example.mymate.data.dto.expense.CurrentStatus

data class HomeInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: CurrentStatus = CurrentStatus()
)