package com.example.mymate

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemReportBinding

class MainReportListAdapter(val nameList: ArrayList<String>, val percentList: ArrayList<Float>, val absList: ArrayList<Int>, val colorItem: ArrayList<Int>): RecyclerView.Adapter<MainReportListAdapter.MainReportListHolder>() {

    inner class MainReportListHolder(val binding: ListitemReportBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
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
            val absText = "${digitprocessing(absitem.toString())}원"
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

    override fun onBindViewHolder(holder: MainReportListHolder, position: Int) {
        var nameitem = nameList[position]
        var percentitem = percentList[position]
        var absitem = absList[position]
        holder.bind(nameitem, percentitem, absitem)
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }
}