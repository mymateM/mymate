package com.example.mymate.data.dto.notification

data class UserActNotiDetail (
    var activity_notification_id: String = "",
    var category_title: String = "",
    var category_image_url: String = "",
    var is_read: Boolean = false,
    var created_at: String = "",
    var trigger: String? = ""
)