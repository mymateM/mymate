package com.example.prototype

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype.databinding.ListItemAlarmBinding

class AlarmActivityAdapter(val context: Context, val listItem: ArrayList<acnoti>): RecyclerView.Adapter<AlarmActivityAdapter.AlarmActivityViewHolder>() {

    private val alarmActivity: AlarmActivity = context as AlarmActivity

    inner class AlarmActivityViewHolder(val binding: ListItemAlarmBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: acnoti) {

            binding.alarmname.text = item.noti_category
            if (item.noti_category == "정산 요청") {
                binding.profileimg.setImageResource(R.drawable.settlement_circle)
                binding.alarmdescription.text = item.user_name + "님이 정산을 기다리고 있어요"
            } else if (item.noti_category == "월별 리포트") {
                binding.profileimg.setImageResource(R.drawable.report_circle)
                binding.alarmdescription.text = "이번 달 지출을 분석했어요!"
            } else if (item.noti_category == "고지서 보관") {
                binding.profileimg.setImageResource(R.drawable.bill_circle)
                binding.alarmdescription.text = "새로운 고지서를 확인하세요"
            } else if (item.noti_category == "예산 초과 경고") {
                binding.profileimg.setImageResource(R.drawable.money_circle)
                binding.alarmdescription.text = "주의하세요! 예산 초과 직전이에요"
            }

            /*var timetxt = item.noti_created_at.split(" ")
            var timefinaltxt = timetxt[0].split("-")
            var timemonth = timefinaltxt[1].toInt()
            var timeday = timefinaltxt[2].toInt()*/

            binding.alarmtime.text = item.noti_created_at

            if (item.noti_is_read == true) {
                binding.alarmunit.setBackgroundResource(R.color.settlementwhite)
            } else if (item.noti_is_read == false) {
                binding.alarmunit.setBackgroundResource(R.color.alarmpurple)
            }

            binding.alarmunit.setOnClickListener {
                if (item.noti_category == "정산 요청") {
                    var gosettlementIntent = Intent(context, SettlementActivity::class.java)
                    ContextCompat.startActivity(context, gosettlementIntent, null)
                    alarmActivity.finish()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmActivityViewHolder {
        val binding = ListItemAlarmBinding.inflate(LayoutInflater.from(context), parent, false)
        return AlarmActivityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: AlarmActivityViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

}