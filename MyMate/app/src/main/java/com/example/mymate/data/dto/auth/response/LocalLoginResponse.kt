package com.example.mymate.data.dto.auth.response

import com.example.mymate.data.dto.auth.UserToken

data class LocalLoginResponse (
    var message: String = "",
    var status: String = "",
    var data: UserToken = UserToken()
)