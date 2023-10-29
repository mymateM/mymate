package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemAlarmBinding
import kotlin.math.exp
import kotlin.math.roundToInt

class AlarmSpdAdapter(val notiList: ArrayList<expenseNoti>): RecyclerView.Adapter<AlarmSpdAdapter.AlarmSpdHolder>() {

    inner class AlarmSpdHolder(val binding: ListitemAlarmBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: expenseNoti) {
            val itemicon = binding.alarmicon
            val itemtype = binding.alarmtype
            val itemdata = binding.alarmdata

            var totalexpense = item.expense_amount.toDouble().roundToInt().toString()
            var textlength = totalexpense.length
            var expense = ""

            while (0 < textlength) {
                var substring1 = ""
                if (textlength == 3) {
                    if (expense == "") {
                        expense = totalexpense.substring(0 until 3)
                    } else {
                        expense = totalexpense.substring(0 until 3) + "," + expense
                    }
                } else if (textlength > 3) {
                    substring1 = totalexpense.substring(textlength - 3 until  textlength)
                    if (expense == "") {
                        expense = substring1
                    } else {
                        expense = substring1 + "," + expense
                    }
                } else if (textlength < 3) {
                    substring1 = totalexpense.substring(0 until textlength)
                    expense = substring1 + "," + expense
                }

                textlength -= 3
            }

            when (item.expense_category_image_url) {
                "FOOD.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_food))
                "ETC.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_life))
                "HOUSE_ITEM.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_life))
                "EDUCATION.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_medical))
                "TRANSPORT.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_traffic))
                "SHOPPING.jpg" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_shopping))
            }

            when (item.expense_category_image_url) {
                "FOOD.jpg" -> itemtype.text = "식비"
                "ETC.jpg" -> itemtype.text = "기타"
                "HOUSE_ITEM.jpg" -> itemtype.text = "생활"
                "EDUCATION.jpg" -> itemtype.text = "의료"
                "TRANSPORT.jpg" -> itemtype.text = "교통"
                "SHOPPING.jpg" -> itemtype.text = "쇼핑"
            }

            itemdata.text = "${item.spender_name}님이 ${expense}원을 지출했어요"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSpdHolder {
        val binding = ListitemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmSpdHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: AlarmSpdHolder, position: Int) {
        val item = notiList[position]
        holder.bind(item)
    }
}