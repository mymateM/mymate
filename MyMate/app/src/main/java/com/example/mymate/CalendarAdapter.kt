package com.example.mymate

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.integerResource
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class CalendarAdapter(val context: Context, val dayList: ArrayList<LocalDate?>, val iteminfo: ArrayList<calendarItem>, val calendarVal: CalendarValues, val date: LocalDate): RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: calendarItem, position: Int, day: LocalDate?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class DayViewHolder(val binding: ListitemCalendarBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, color: String, info: calendarItem) {
            val day = binding.dayText
            val spend = binding.billText
            day.text = item
            spend.isInvisible = true
            //spend.text = item
            //text color
            if (info.lastornext) {
                day.setTextColor(Color.TRANSPARENT)
                spend.setTextColor(Color.TRANSPARENT)
                binding.background.isGone = true
            } else {
                if(info.startorEnd) {
                    binding.background.isGone = false
                    binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_background))
                } else if (info.middle) {
                    binding.background.isGone = true
                    binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    binding.calendarItem.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    binding.background.isGone = true
                    binding.dayText.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    binding.calendarItem.setBackgroundColor(Color.TRANSPARENT)
                }
            }

            if (onItemClickListener != null) {
                binding.calendarItem.setOnClickListener {
                    onItemClickListener?.onItemClick(info, absoluteAdapterPosition, dayList[absoluteAdapterPosition])
                    calendarVal.setDay(absoluteAdapterPosition)
                    calendarVal.daycheck()
                    /*if (calendarVal.firstDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
                    } else if (calendarVal.lastDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
                    } else {
                        info.startorEnd = false
                        info.middle = false
                    }*/

                    if (calendarVal.firstDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
                        calendarVal.lastDay = absoluteAdapterPosition
                    } else if (calendarVal.lastDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
                        calendarVal.firstDay = absoluteAdapterPosition
                    } else {
                        info.startorEnd = false
                        info.middle = false
                    }

                    notifyDataSetChanged()
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
        if (dayList[position] == null) {
            daytext = ""
        } else {
            daytext = day?.dayOfMonth.toString()
        }

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

        if (position != calendarVal.firstDay || position != calendarVal.lastDay) {
            iteminfo[position].middle = false
            iteminfo[position].startorEnd = false
        } else {
            iteminfo[position].startorEnd = true
        }
        /*if (position > calendarVal.firstDay && position < calendarVal.lastDay) {
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
        }*/

        /*var selectedDate = LocalDate.now()
        var formatter = DateTimeFormatter.ofPattern("dd")
        var date = selectedDate.format(formatter)
        if (date.toInt() == position + 1) {
            iteminfo[position].startorEnd = true
            iteminfo[position].middle = false
        } else {
            iteminfo[position].middle = false
            iteminfo[position].startorEnd = false
        }*/

        holder.bind(daytext, colortext, iteminfo[position])
    }
}