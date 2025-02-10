package com.example.mymate.data.dto.bill.response

import com.example.mymate.data.dto.bill.BillCategoryWrapper

data class BillCategoryResponse (
    var message: String = "",
    var status: String = "",
    var data: BillCategoryWrapper = BillCategoryWrapper()
)