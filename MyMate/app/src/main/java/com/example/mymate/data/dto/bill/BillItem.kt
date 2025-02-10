package com.example.mymate.data.dto.bill

import android.graphics.drawable.Drawable

data class BillItem(
    var date: String = "",
    var name: String = "",
    var amount: String = "",
    var drawable: Drawable
)