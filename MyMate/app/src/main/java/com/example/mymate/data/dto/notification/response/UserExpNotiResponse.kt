package com.example.mymate.data.dto.notification.response

import com.example.mymate.data.dto.notification.UserExpNoti

data class UserExpNotiResponse (
    var message: String = "",
    var status: String = "",
    var data: UserExpNoti = UserExpNoti()
)