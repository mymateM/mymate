package com.example.mymate.data.dto.notification

data class UserExpNotiDetail (
    var expense_notification_id: String = "",
    var expense_category_image_url: String = "",
    var created_at: String = "",
    var is_read: Boolean = false,
    var expense_amount: String = "",
    var spender_name: String = ""
)