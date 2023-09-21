package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemDailyspendBinding

class SpendingAdapter(val context: Context, val expenseList: ArrayList<householdExpenseDetail>): RecyclerView.Adapter<SpendingAdapter.ExpenseViewHolder>() {


    inner class ExpenseViewHolder(val binding: ListitemDailyspendBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: householdExpenseDetail) {
            val categoryImage = binding.spendingCategory
            val amount = binding.spendingAmount
            val store = binding.spendingStore
            val profileStart = binding.spendingProfileStart
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ListitemDailyspendBinding.inflate(LayoutInflater.from(context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        var item = expenseList[position]
        holder.bind(item)
    }
}