package com.example.mymate.data.dto.auth

data class UserToken (
    var access_token: String = "",
    var refresh_token: String = ""
)