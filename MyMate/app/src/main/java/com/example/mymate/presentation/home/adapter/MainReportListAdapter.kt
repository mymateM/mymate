package com.example.mymate.presentation.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.databinding.ListitemReportBinding
import com.example.mymate.util.FontManager
import com.example.mymate.util.getTypefaceSpan
import java.text.DecimalFormat

class MainReportListAdapter(val nameList: ArrayList<String>, val percentList: ArrayList<Float>, val absList: ArrayList<Int>, val colorItem: ArrayList<Int>): RecyclerView.Adapter<MainReportListAdapter.MainReportListHolder>() {

    inner class MainReportListHolder(val binding: ListitemReportBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(nameitem: String, percentitem: Float, absitem: Int) {
            val categoryImage = binding.icon
            when (nameitem) {
                "식비" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_food))
                "생활" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_life))
                "쇼핑" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_shopping))
                "교통" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_traffic))
                "의료" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_medical))
                "고지서" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_bill))
                "교육" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_education))
                "기타" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_etc))
            }

            binding.categorytext.text = nameitem
            val percentText = "${percentitem * 100}%"
            val absText = SpannableStringBuilder("${DecimalFormat("#,###").format(absitem)}원")
            absText.setSpan(FontManager.montserratSemiBold.getTypefaceSpan(), 0, absText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            absText.setSpan(FontManager.suitSemiBold.getTypefaceSpan(), absText.length - 1, absText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.percentText.text = percentText
            binding.absText.text = absText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainReportListHolder {
        val binding = ListitemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainReportListHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: MainReportListHolder, position: Int) {
        var nameitem = nameList[position]
        var percentitem = percentList[position]
        var absitem = absList[position]
        holder.bind(nameitem, percentitem, absitem)
    }
}