package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.prototype.databinding.ListItemMainsettlementBinding

class homeListAdapter(val context: Context, val homeList: ArrayList<listItem>) : BaseAdapter() {

    private var mBinding : ListItemMainsettlementBinding? = null
    private val binding get() = mBinding!!

    private var thiscontext: MainActivity = context as MainActivity

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        mBinding = ListItemMainsettlementBinding.inflate(LayoutInflater.from(context))

        val profile = binding.mainlistprofile
        val maintext = binding.listmaintext
        val subtext = binding.listsubtext
        val guideline = binding.listguide
        val percent = binding.percentText
        val percentcolor = binding.horizontalbar

        val item = homeList[position]

        profile.setImageResource(item.profilepic)
        maintext.text = item.name
        subtext.text = item.title
        guideline.setGuidelinePercent(item.ratio.toFloat()/100)
        percent.text = item.ratio + "%"
        if (item.flag == "pink") {
            percentcolor.setImageResource(R.drawable.horizontalbar_pink)
            subtext.setTextColor(item.color)
            percent.setTextColor(item.color)
        } else if (item.flag == "blue") {
            percentcolor.setImageResource(R.drawable.horizontalbar_blue)
            subtext.setTextColor(item.color)
            percent.setTextColor(item.color)
        } else if (item.flag == "green") {
            percentcolor.setImageResource(R.drawable.horizontalbar_green)
            subtext.setTextColor(item.color)
            percent.setTextColor(item.color)
        } else if (item.flag == "yellow") {
            percentcolor.setImageResource(R.drawable.horizontalbar_yellow)
            subtext.setTextColor(item.color)
            percent.setTextColor(item.color)
        } else {
            percentcolor.setImageResource(R.drawable.horizontalbar_purple)
            subtext.setTextColor(item.color)
            subtext.setTextColor(item.color)
        }

        return mBinding!!.root
    }

    override fun getCount(): Int {
        return homeList.size
    }

    override fun getItem(position: Int): Any {
        return homeList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }


}