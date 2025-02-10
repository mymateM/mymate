package com.example.mymate.data.dto.setting.response

import com.example.mymate.data.dto.setting.Budget

data class UserBudgetResponse (
    var message: String = "",
    var status: String = "",
    var data: Budget = Budget()
)