package com.example.mymate.data.dto.notification.response

import com.example.mymate.data.dto.notification.UserActNoti

data class UserActNotiResponse (
    var message: String = "",
    var status: String = "",
    var data: UserActNoti = UserActNoti()
)