package com.example.mymate.data.dto.setting

data class UserAccount (
    var account_bank: String = "",
    var account_number: String = "",
    var account_image_url: String = "",
    var members: ArrayList<MemberAccount> = ArrayList()
)