package com.example.mymate.data.dto.bill.request

import com.example.mymate.data.dto.bill.VirtualAccountDetail

data class BillWriteRequest (
    var bill_payment_date: String = "",
    var bill_image: String = "",
    var bill_payment_amount: String = "",
    var bill_store: String = "",
    var bill_category_title: String = "",
    var bill_memo: String = "",
    var virtual_accounts: ArrayList<VirtualAccountDetail> = ArrayList()
)