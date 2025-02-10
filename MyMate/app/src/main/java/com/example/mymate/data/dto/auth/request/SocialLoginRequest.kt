package com.example.mymate.data.dto.auth.request

data class SocialLoginRequest (
    var socialAuthType: String = "",
    var socialAccessToken: String = ""
)