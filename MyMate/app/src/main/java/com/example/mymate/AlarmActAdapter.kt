package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.mymate.databinding.ListitemAlarmBinding

class AlarmActAdapter(val notiList: ArrayList<activityNoti>): RecyclerView.Adapter<AlarmActAdapter.AlarmActHolder>() {

    inner class AlarmActHolder(val binding: ListitemAlarmBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: activityNoti) {
            val itemicon = binding.alarmicon
            val itemtype = binding.alarmtype
            val itemdata = binding.alarmdata

            when (item.category_title) {
                "초대 수락" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_newmate))
                "정산 디데이" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_money))
                "정산일 변경" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_money))
                "예산 변경" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_money))
                "정산 비율 변경" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_money))
                "고지서" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_bill))
                "경고" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_alert))
                "예산 초과 경고" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_alert))
                "월별 리포트" -> itemicon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_money)) //TODO:리포트 아이콘 현재 없음
            }

            when (item.category_title) {
                "정산 디데이" -> itemtype.text = "정산 D-Day"
                else -> itemtype.text = item.category_title
            }

            when (item.category_title) {
                "초대 수락" -> itemdata.text = "해피하우스에 새로운 ${item.trigger} 메이트가 들어왔어요"
                "정산 디데이" -> itemdata.text = "정산일이 다가왔어요! 정산을 하러 가볼까요?"
                "정산일 변경" -> itemdata.text = "정산일이 바뀌었어요!"
                "예산 변경" -> itemdata.text = "이번 정산부터 예산은 원이에요" //TODO: 같은 타이틀에 문구 2개
                "정산 비율 변경" -> itemdata.text = "비율 변경 소식이에요! 내 비율은 얼마일까요?"
                "고지서" -> itemdata.text = "새로운 고지서를 확인하세요!"
                "경고" -> itemdata.text = "앗! 지출이 예산을 넘겼어요"
                "예산 초과 경고" -> itemdata.text = "앗! 지출이 예산을 넘겼어요" //TODO: 같은 타이틀에 문구 2개
                "월별 리포트" -> itemdata.text = "한달 지출을 분석했어요!"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmActHolder {
        val binding = ListitemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmActHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: AlarmActHolder, position: Int) {
        val item = notiList[position]
        holder.bind(item)
    }
}