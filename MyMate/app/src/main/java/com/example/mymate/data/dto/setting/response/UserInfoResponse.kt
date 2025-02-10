package com.example.mymate.data.dto.setting.response

import com.example.mymate.data.dto.setting.UserInfo

data class UserInfoResponse (
    var message: String = "",
    var status: String = "",
    var data: UserInfo = UserInfo()
)