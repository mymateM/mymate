package com.example.mymate.presentation.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.databinding.ListitemSearchlistcontainerBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchListContainerAdapter(val searchList: ArrayList<ArrayList<ExpenseSummary>>): RecyclerView.Adapter<SearchListContainerAdapter.SearchListContainerHolder>() {
    lateinit var context: Context

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    inner class SearchListContainerHolder(val binding: ListitemSearchlistcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<ExpenseSummary>, context: Context) {
            if (item.isNotEmpty()) {
                val today = LocalDate.now()
                val expenseDate = LocalDate.parse(item[0].expenseDate, formatter)
                val dayText = if (today.format(formatter) == item[0].expenseDate) {
                    "${today.monthValue}월 ${today.dayOfMonth}일 (오늘)"
                } else {
                    "${expenseDate.monthValue}월 ${expenseDate.dayOfMonth}일"
                }
                binding.daytext.text = dayText
                binding.searchlist.layoutManager = LinearLayoutManager(context)
                binding.searchlist.adapter = SearchListAdapter(item)
            } else {
                binding.root.isGone = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListContainerHolder {
        val binding = ListitemSearchlistcontainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return SearchListContainerHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchListContainerHolder, position: Int) {
        var item = searchList[position]
        holder.bind(item, context)
    }

}