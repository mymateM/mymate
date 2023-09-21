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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemCalendarBinding
import java.time.LocalDate

class CalendarAdapter(val context: Context, val dayList: ArrayList<LocalDate?>, val iteminfo: ArrayList<calendarItem>, val calendarVal: CalendarValues): RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

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
            day.text = item
            spend.text = item
            //text color
            if (info.lastornext) {
                day.setTextColor(Color.GRAY)
            } else if (color == "blue") {
                day.setTextColor(Color.BLUE)
            } else if (color == "red") {
                day.setTextColor(Color.RED)
            } else {
                day.setTextColor(Color.BLACK)
            }

            if(info.startorEnd) {
                binding.calendarItem.setBackgroundColor(ContextCompat.getColor(context, R.color.purplevivid_graph))
            } else if (info.middle) {
                binding.calendarItem.setBackgroundColor(ContextCompat.getColor(context, R.color.purplelight_graph))
            } else {
                binding.calendarItem.setBackgroundColor(Color.TRANSPARENT)
            }

            if (onItemClickListener != null) {
                binding.calendarItem.setOnClickListener {
                    onItemClickListener?.onItemClick(info, absoluteAdapterPosition)
                    calendarVal.setDay(absoluteAdapterPosition)
                    calendarVal.daycheck()
                    Log.d("beforedaycheck", absoluteAdapterPosition.toString() + " " + calendarVal.firstDay.toString() + " " + calendarVal.lastDay.toString())
                    if (calendarVal.firstDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
                    } else if (calendarVal.lastDay == absoluteAdapterPosition) {
                        info.startorEnd = true
                        info.middle = false
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        var day = dayList[position]
        var daytext = ""
        var colortext = "black"
        if (dayList[position] == null) {
            daytext = ""
        } else {
            daytext = day?.dayOfMonth.toString()
        }
        if ((position + 1) % 7 == 0) {
            colortext = "blue"
        } else if (position == 0 || position % 7 == 0) {
            colortext = "red"
        } else {
            colortext = "black"
        }

        if (position > calendarVal.firstDay && position < calendarVal.lastDay) {
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