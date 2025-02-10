package com.example.mymate.data.dto.setting.response

import com.example.mymate.data.dto.setting.UserAccount

data class UserAccountResponse (
    var message: String = "",
    var status: String = "",
    var data: UserAccount = UserAccount()
)