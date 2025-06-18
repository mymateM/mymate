package com.example.mymate.presentation.search

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.databinding.ListitemSearchlistBinding
import com.example.mymate.util.Category
import com.example.mymate.util.CategoryIconProvider

class SearchListAdapter(val searchList: ArrayList<ExpenseSummary>): RecyclerView.Adapter<SearchListAdapter.SearchListHolder>() {

    inner class SearchListHolder(val binding: ListitemSearchlistBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpenseSummary) {
            binding.categoryicon.setImageDrawable(CategoryIconProvider.getIconImage(context,
                Category.fromDisplayName(item.expenseCategoryName)))
            binding.billamount.text = DecimalFormat("#,###").format(item.expenseAmount)
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
        val item = searchList[position]
        holder.bind(item)
    }

}