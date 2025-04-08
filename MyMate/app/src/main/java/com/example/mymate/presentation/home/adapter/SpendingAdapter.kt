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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.databinding.ListitemSpendingBinding
import java.text.DecimalFormat

class SpendingAdapter(): ListAdapter<ExpenseSummary, SpendingAdapter.ExpenseViewHolder>(diffUtil) {

    private var onItemClickListener: OnItemClickListener? = null
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(item: ExpenseSummary, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class ExpenseViewHolder(val binding: ListitemSpendingBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(item: ExpenseSummary) {
            val categoryImage = binding.spendingicon
            val amount = binding.spendingAmount
            val store = binding.spendingStore

            val amountText = SpannableStringBuilder("${DecimalFormat("#,###").format(item.expenseAmount.toInt())}원")
            val montSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_semibold), Typeface.NORMAL)
            val suitSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_semibold), Typeface.NORMAL)
            amountText.setSpan(TypefaceSpan(montSemiBoldTypeface), 0, amountText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            amountText.setSpan(TypefaceSpan(suitSemiBoldTypeface), amountText.length - 1, amountText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            amount.text = amountText
            store.text = item.expenseStore

            when (item.expenseCategoryName) {
                "식비" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_food))
                "생활" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_life))
                "쇼핑" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_shopping))
                "교통" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_traffic))
                "의료" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_medical))
                "고지서" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_bill))
                "교육" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.alarmicon_education
                ))
                "기타" -> categoryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.alarmicon_etc))
            }

            if (onItemClickListener != null) {
                binding.spendinglistcontainer.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ListitemSpendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ExpenseViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ExpenseSummary>() {
            override fun areItemsTheSame(p0: ExpenseSummary, p1: ExpenseSummary) = p0 == p1

            override fun areContentsTheSame(p0: ExpenseSummary, p1: ExpenseSummary) = p0 == p1

        }
    }
}