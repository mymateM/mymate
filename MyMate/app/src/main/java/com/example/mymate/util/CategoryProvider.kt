package com.example.mymate.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.mymate.R

enum class Category(val displayName: String) {
    FOOD("식비"),
    LIFE("생활"),
    SHOPPING("쇼핑"),
    TRANSPORT("교통"),
    MEDICAL("의료"),
    BILL("고지서"),
    EDUCATION("교육"),
    ETC("기타");

    companion object {
        fun fromDisplayName(value: String): Category {
            return values().find { it.name == value } ?: ETC
        }
    }
}

object CategoryColorProvider {
    val categoryColorMap = mapOf(
        Category.FOOD to R.color.pie_yellow,
        Category.LIFE to R.color.pie_purple,
        Category.SHOPPING to R.color.pie_pink,
        Category.TRANSPORT to R.color.pie_blue,
        Category.MEDICAL to R.color.pie_red,
        Category.BILL to R.color.pie_yellow,
        Category.EDUCATION to R.color.pie_green,
        Category.ETC to R.color.pie_gray
    )

    private fun getColorRes(category: Category): Int {
        return categoryColorMap[category] ?: R.color.pie_gray
    }

    fun getColorInt(context: Context, category: Category): Int {
        return ContextCompat.getColor(context, getColorRes(category))
    }
}

object CategoryIconProvider {
    val categoryIconMap = mapOf(
        Category.FOOD to R.drawable.alarmicon_food,
        Category.LIFE to R.drawable.alarmicon_life,
        Category.SHOPPING to R.drawable.alarmicon_shopping,
        Category.TRANSPORT to R.drawable.alarmicon_traffic,
        Category.MEDICAL to R.drawable.alarmicon_medical,
        Category.BILL to R.drawable.alarmicon_bill,
        Category.EDUCATION to R.drawable.alarmicon_education,
        Category.ETC to R.drawable.alarmicon_etc
    )

    fun getIconRes(category: Category): Int {
        return categoryIconMap[category] ?: R.drawable.alarmicon_etc
    }

    fun getIconImage(context: Context, category: Category): Drawable {
        return ContextCompat.getDrawable(context, getIconRes(category))!!
    }
}