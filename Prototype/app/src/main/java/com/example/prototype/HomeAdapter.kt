package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype.databinding.ListItemMainsettlementBinding

class HomeAdapter(val context: Context, val homelist: ArrayList<listItem>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(val binding: ListItemMainsettlementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: listItem) {
            val profile = binding.mainlistprofile
            val maintext = binding.listmaintext
            val subtext = binding.listsubtext
            val guideline = binding.listguide
            val percent = binding.percentText
            val percentcolor = binding.horizontalbar

            profile.setImageResource(item.profilepic)
            maintext.text = item.name
            subtext.text = item.title
            if (item.ratio.toFloat() > 100) {
                guideline.setGuidelinePercent(1.toFloat())
            } else {
                guideline.setGuidelinePercent(item.ratio.toFloat()/100)
            }
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ListItemMainsettlementBinding.inflate(LayoutInflater.from(context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return homelist.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(homelist[position])
    }
}