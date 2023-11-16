package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemAlarmcontainerBinding
import java.text.SimpleDateFormat
import java.time.LocalDate

class AlarmActContainerAdapter(val alarmList: ArrayList<ArrayList<activityNoti>>): RecyclerView.Adapter<AlarmActContainerAdapter.AlarmActContainerHolder>() {

    inner class AlarmActContainerHolder(val binding: ListitemAlarmcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<activityNoti>) {
            val datetocompare = SimpleDateFormat("yyyy-mm-dd")
            val datetoshow = SimpleDateFormat("mm월 dd일")
            val datecompare = datetocompare.parse(item[0].created_at)
            val dateshow = datetoshow.format(datecompare)
            val today = LocalDate.now().toString()
            val todaytocompare = datetocompare.parse(today)

            if (datecompare == todaytocompare) {
                binding.alarmdate.text = dateshow + " 오늘"
            } else {
                binding.alarmdate.text = dateshow
            }

            val adapter = AlarmActAdapter(item)
            val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            binding.alarmlist.layoutManager = manager
            binding.alarmlist.adapter = adapter.apply {
                setOnItemClickListener(object : AlarmActAdapter.OnItemClickListener {
                    override fun onItemClick(item: activityNoti, position: Int) {
                        if (item.category_title == "정산 디데이") {
                            context.startActivity(Intent(context, SettlementActivity::class.java))
                        }
                    }

                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmActContainerHolder {
        val binding = ListitemAlarmcontainerBinding.inflate(LayoutInflater.from(parent.context))
        return AlarmActContainerHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: AlarmActContainerHolder, position: Int) {
        val item = alarmList[position]
        holder.bind(item)
    }
}