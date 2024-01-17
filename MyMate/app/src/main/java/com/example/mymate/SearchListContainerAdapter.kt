package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemSearchlistcontainerBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchListContainerAdapter(val searchList: ArrayList<ArrayList<ExpenseList>>): RecyclerView.Adapter<SearchListContainerAdapter.SearchListContainerHolder>() {
    lateinit var context: Context

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    inner class SearchListContainerHolder(val binding: ListitemSearchlistcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<ExpenseList>, context: Context, list: ArrayList<ArrayList<ExpenseList>>) {
            if (item.isNotEmpty()) {
                if (LocalDate.now().format(formatter) == item[0].expenseDate) {
                    val today = "${LocalDate.now().monthValue}월 ${LocalDate.now().dayOfMonth}일 (오늘)"
                    binding.daytext.text = today
                } else {
                    val date = LocalDate.parse(item[0].expenseDate, formatter)
                    val today = "${date.monthValue}월 ${date.dayOfMonth}일"
                    binding.daytext.text = today
                }
                val adapter = SearchListAdapter(item)
                val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                binding.searchlist.layoutManager = manager
                binding.searchlist.adapter = adapter
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
        holder.bind(item, context, searchList)
    }

}