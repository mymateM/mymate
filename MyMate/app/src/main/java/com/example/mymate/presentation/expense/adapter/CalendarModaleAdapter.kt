package com.example.mymate.presentation.expense.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.databinding.ListitemCalendarBinding
import com.example.mymate.presentation.expense.CalendarValues
import java.time.LocalDate
import java.time.YearMonth

class CalendarModaleAdapter(val context: Context, private val dayList: ArrayList<LocalDate?>, val calendarVal: CalendarValues, val month: Int): RecyclerView.Adapter<CalendarModaleAdapter.DayViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(value: CalendarValues, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun selectionState(index: Int): Int {
        when {
            calendarVal.firstDay < 0 && calendarVal.lastDay < 0 -> return NONE
            (calendarVal.firstDay < 0 || calendarVal.lastDay < 0)
                    && (calendarVal.firstDay == index || calendarVal.lastDay == index) -> return SINGLE
            calendarVal.firstDay < 0 || calendarVal.lastDay < 0 -> return NONE
            index == calendarVal.firstDay -> return FIRST
            index == calendarVal.lastDay -> return LAST
            index > calendarVal.firstDay && index < calendarVal.lastDay -> return MID
            else -> return NONE
        }
    }

    inner class DayViewHolder(val binding: ListitemCalendarBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalDate?) {

            binding.billText.isInvisible = true
            if (item == null || item.monthValue != month) {
                binding.dayText.isInvisible = true
                binding.background.isInvisible = true
                binding.backgroundmodale.isInvisible = true
                return
            }

            binding.run {
                dayText.text = item.dayOfMonth.toString()
                dayText.isInvisible = false
                background.isInvisible = true
                backgroundmodale.isInvisible = false
                dayText.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                when (selectionState(absoluteAdapterPosition)) {
                    FIRST -> backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_start))
                    LAST -> backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_end))
                    MID -> backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_middle))
                    SINGLE -> {
                        background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_background))
                        background.isInvisible = false
                        backgroundmodale.isInvisible = true
                    }
                    NONE -> backgroundmodale.isInvisible = true
                }
            }

            if (onItemClickListener != null) {
                binding.calendarItem.setOnClickListener {
                    if (item.monthValue == month) {
                        calendarVal.setDay(absoluteAdapterPosition)
                        calendarVal.updateSelectedDays()
                        onItemClickListener?.onItemClick(calendarVal, absoluteAdapterPosition)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ListitemCalendarBinding.inflate(LayoutInflater.from(context), parent, false)
        return DayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(dayList[position])
    }

    companion object {
        val FIRST = 1
        val LAST = 2
        val MID = 3
        val SINGLE = 4
        val NONE = 5
    }
}