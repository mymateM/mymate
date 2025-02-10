package com.example.mymate.data.dto.bill

data class BillDetail (
    var bill_category: String = "",
    var bill_payment_amount: String = "",
    var bill_image_url: String = "",
    var bill_payment_date: String = "",
    var bill_memo: String? = "",
    var register_date: String = ""
)