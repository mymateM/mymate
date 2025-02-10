package com.example.mymate.data.dto.bill

data class BillSummary (
    var bill_id: String = "",
    var bill_image_url: String = "",
    var bill_payment_date: String = "",
    var bill_store: String = "",
    var bill_payment_amount: String = ""
)