package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.prototype.databinding.ListItemAlarmBinding

class listAdapter(val context:Context, val alarmList: ArrayList<alarms>) : BaseAdapter() {

    private var mBinding : ListItemAlarmBinding? = null
    private val binding get() = mBinding!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) : View {
        mBinding = ListItemAlarmBinding.inflate(LayoutInflater.from(context))

        val profile = binding.profileimg
        val name = binding.alarmname
        val description = binding.alarmdescription
        val alarmtime = binding.alarmtime

        val alarm = alarmList[position]

        profile.setImageResource(alarm.profilepic)
        name.text = alarm.name
        description.text = alarm.description
        alarmtime.text = alarm.alarmtime

        return mBinding!!.root

    }

    override fun getItem(position: Int): Any {
        return alarmList[position]
    }

    override fun getCount(): Int {
        return alarmList.size
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

}