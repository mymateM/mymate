package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemSpendingBinding

class SpendingAdapter(val context: Context, val expenseList: ArrayList<ExpenseList>): RecyclerView.Adapter<SpendingAdapter.ExpenseViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: ExpenseList, position: Int)
    }

    inner class ExpenseViewHolder(val binding: ListitemSpendingBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(item: ExpenseList) {
            val categoryImage = binding.spendingicon
            val amount = binding.spendingAmount
            val store = binding.spendingStore

            val amounttext = SpannableStringBuilder("${digitprocessing(item.expenseAmount)}원")
            val montSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_semibold), Typeface.NORMAL)
            val suitSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_semibold), Typeface.NORMAL)
            amounttext.setSpan(TypefaceSpan(montSemiBoldTypeface), 0, amounttext.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            amounttext.setSpan(TypefaceSpan(suitSemiBoldTypeface), amounttext.length - 1, amounttext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            amount.text = amounttext
            store.text = item.expenseStore

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

            if (onItemClickListener != null) {
                binding.spendinglistcontainer.setOnClickListener {
                    val intent = Intent(context, SpendingDetailActivity::class.java)
                    intent.putExtra("id", item.expenseId)
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ListitemSpendingBinding.inflate(LayoutInflater.from(context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        var item = expenseList[position]
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