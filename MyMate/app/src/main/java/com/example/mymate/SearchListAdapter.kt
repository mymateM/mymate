package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemSearchlistBinding
import com.example.mymate.databinding.ListitemSearchlistcontainerBinding

class SearchListAdapter(val searchList: ArrayList<ExpenseList>): RecyclerView.Adapter<SearchListAdapter.SearchListHolder>() {

    inner class SearchListHolder(val binding: ListitemSearchlistBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpenseList) {
            val categoryImage = binding.categoryicon
            when (item.expenseCategoryName) {
                "식비" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_food))
                "생활" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_life))
                "쇼핑" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_shopping))
                "교통" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_traffic))
                "의료" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_medical))
                "고지서" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_bill))
                "교육" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_education))
                "기타" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_etc))
            }
            binding.billamount.text = digitprocessing(item.expenseAmount)
            binding.marketname.text = item.expenseStore
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {
        val binding = ListitemSearchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchListHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {
        var item = searchList[position]
        holder.bind(item)
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