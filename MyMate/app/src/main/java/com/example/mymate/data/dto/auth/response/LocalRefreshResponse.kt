package com.example.mymate.data.dto.auth.response

data class LocalRefreshResponse (
    var access_token: String = "",
    var refresh_token: String = ""
)