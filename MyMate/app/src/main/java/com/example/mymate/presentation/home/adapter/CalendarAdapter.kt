package com.example.mymate.presentation.home.adapter

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.data.dto.expense.CalendarInfo
import com.example.mymate.databinding.ListitemCalendarBinding

class CalendarAdapter(): ListAdapter<CalendarInfo, CalendarAdapter.DayViewHolder>(diffUtil) {

    private var onItemClickListener: OnItemClickListener? = null
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(item: String, position: Int, day: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class DayViewHolder(val binding: ListitemCalendarBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarInfo) {
            val day = binding.dayText
            val spend = binding.billText
            day.text = "${item.days}"
            day.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            spend.isInvisible = true
            binding.background.isGone = true

            if (day.text == "0") {
                day.isInvisible = true
                spend.isInvisible = true
                binding.background.isInvisible = true
            }

            if (item.dayExpenses != 0) {
                spend.isInvisible = false
                spend.text = "-${DecimalFormat("#,###").format(item.dayExpenses)}"
            }

            if (item.today) {
                day.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.background.isInvisible = false
                binding.background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_background))
            }

            if (onItemClickListener != null) {
                binding.calendarItem.setOnClickListener {
                    onItemClickListener?.onItemClick(item.days.toString(), absoluteAdapterPosition, item.days)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ListitemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CalendarInfo>() {
            override fun areItemsTheSame(p0: CalendarInfo, p1: CalendarInfo) = p0 == p1

            override fun areContentsTheSame(p0: CalendarInfo, p1: CalendarInfo) = p0 == p1

        }
    }
}