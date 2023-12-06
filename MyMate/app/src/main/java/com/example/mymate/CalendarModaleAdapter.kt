package com.example.mymate

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemCalendarBinding
import okhttp3.internal.notify
import java.time.LocalDate
import java.time.YearMonth

class CalendarModaleAdapter(val context: Context, val dayList: ArrayList<LocalDate?>, val iteminfo: ArrayList<calendarItem>, val calendarVal: CalendarValues): RecyclerView.Adapter<CalendarModaleAdapter.DayViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private val yearMonth = YearMonth.from(dayList[8])
    private val dayOfWeek = dayList[8]!!.dayOfWeek!!.value
    private val length = dayList[8]!!.lengthOfMonth()

    interface OnItemClickListener {
        fun onItemClick(item: calendarItem, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    inner class DayViewHolder(val binding: ListitemCalendarBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, color: String, info: calendarItem) {
            val day = binding.dayText
            val spend = binding.billText
            spend.isInvisible = true
            day.text = item
            binding.background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_background))
            binding.background.isInvisible = true
            binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.black_text))

            if (info.lastornext) {
                day.setTextColor(Color.TRANSPARENT)
            } else {
                if (info.startorEnd && calendarVal.firstDay == -1) {
                    binding.background.isInvisible = false
                    binding.backgroundmodale.isInvisible = true
                    binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                } else if (info.startorEnd && calendarVal.lastDay == -1) {
                    binding.background.isInvisible = false
                    binding.backgroundmodale.isInvisible = true
                    binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                } else {
                    binding.background.isInvisible = true
                    binding.backgroundmodale.isInvisible = false
                    if (absoluteAdapterPosition == calendarVal.firstDay && info.startorEnd) {
                        binding.backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_start))
                        binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                    } else if (absoluteAdapterPosition == calendarVal.lastDay && info.startorEnd) {
                        binding.backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_end))
                        binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
                    } else if (info.middle) {
                        binding.backgroundmodale.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_period_middle))
                        binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    } else {
                        binding.backgroundmodale.isInvisible = true
                    }
                }
            }

            if (calendarVal.lastDay == -1 && calendarVal.firstDay == -1) {
                binding.backgroundmodale.isInvisible = true
            }

            if (onItemClickListener != null) {
                binding.calendarItem.setOnClickListener {
                    if (!iteminfo[absoluteAdapterPosition].lastornext) {
                        calendarVal.setDay(absoluteAdapterPosition)
                        calendarVal.daycheck()

                        if (calendarVal.firstDay == absoluteAdapterPosition) {
                            info.startorEnd = true
                            info.middle = false
                            info.lastornext = false
                        } else if (calendarVal.lastDay == absoluteAdapterPosition) {
                            info.startorEnd = true
                            info.middle = false
                            info.lastornext = false
                        } else {
                            info.startorEnd = false
                            info.middle = false
                            info.lastornext = false
                        }
                        onItemClickListener?.onItemClick(info, absoluteAdapterPosition)
                        notifyItemRangeChanged(dayOfWeek + 1, length + 1)
                        //notifyDataSetChanged()
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
        var day = dayList[position]
        var daytext = ""
        var colortext = "black"

        daytext = day?.dayOfMonth.toString()

        when (position % 7) {
            0 -> colortext = "SUN"
            1 -> colortext = "MON"
            2 -> colortext = "TUE"
            3 -> colortext = "WED"
            4 -> colortext = "THU"
            5 -> colortext = "FRI"
            6 -> colortext = "SAT"
        }

        if (position == 0) {
            colortext = "SUN"
        }

        if (position <= dayOfWeek) {
            iteminfo[position].middle = false
            iteminfo[position].startorEnd = false
        } else if (position > calendarVal.firstDay && position < calendarVal.lastDay) {
            iteminfo[position].middle = true
            iteminfo[position].startorEnd = false
        } else if (position == calendarVal.firstDay) {
            iteminfo[position].startorEnd = true
            iteminfo[position].middle = false
            if (calendarVal.firstDay < 0) {
                iteminfo[position].startorEnd = false
            }
        } else if (position == calendarVal.lastDay) {
            iteminfo[position].startorEnd = true
            iteminfo[position].middle = false
            if (calendarVal.lastDay < 0) {
                iteminfo[position].startorEnd = false
            }
        } else {
            iteminfo[position].startorEnd = false
            iteminfo[position].middle = false
        }
        if (calendarVal.firstDay < 0 || calendarVal.lastDay < 0) {
            iteminfo[position].middle = false
        }

        holder.bind(daytext, colortext, iteminfo[position])
    }
}