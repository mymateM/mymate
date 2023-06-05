package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype.databinding.ListItemAlarmBinding

class AlarmSpendingAdapter(val context: Context, val listItem: ArrayList<exnoti>): RecyclerView.Adapter<AlarmSpendingAdapter.AlarmSpendingViewHolder>() {

    inner class AlarmSpendingViewHolder(val binding: ListItemAlarmBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: exnoti) {

            binding.alarmname.text = item.expense_category

            if (item.expense_category == "식비") {
                binding.profileimg.setImageResource(R.drawable.food_circle)
            } else if (item.expense_category == "생활") {
                binding.profileimg.setImageResource(R.drawable.home_circle)
            } else if (item.expense_category == "쇼핑") {
                binding.profileimg.setImageResource(R.drawable.shopping_circle)
            }

            var totaltext = item.expense_amount
            var textlength = totaltext.length
            var totalexpense = ""
            while (0 < textlength) {
                var substring1 = ""
                if (textlength == 3) {
                    if (totalexpense == "") {
                        totalexpense = totaltext.substring(0 until 3)
                    } else {
                        totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                    }
                } else if (textlength > 3) {
                    substring1 = totaltext.substring(textlength - 3 until textlength)
                    if (totalexpense == "") {
                        totalexpense = substring1
                    } else {
                        totalexpense = substring1 + "," + totalexpense
                    }
                } else if (textlength < 3) {
                    substring1 = totaltext.substring(0 until textlength)
                    totalexpense = substring1 + "," + totalexpense
                }

                textlength = textlength - 3
            }

            binding.alarmdescription.text = item.sender_name + "님이 " + totalexpense + "원을 지출했어요"

            /*var timetxt = item.created_at.split(" ")
            var timefinaltxt = timetxt[0].split("-")
            var timemonth = timefinaltxt[1].toInt()
            var timeday = timefinaltxt[2].toInt()*/

            binding.alarmtime.text = item.created_at

            if (item.noti_is_read == true) {
                binding.alarmunit.setBackgroundResource(R.color.settlementwhite)
            } else if (item.noti_is_read == false) {
                binding.alarmunit.setBackgroundResource(R.color.alarmpurple)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSpendingViewHolder {
        val binding = ListItemAlarmBinding.inflate(LayoutInflater.from(context), parent, false)
        return AlarmSpendingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: AlarmSpendingViewHolder, position: Int) {
        holder.bind(listItem[position])
    }
}