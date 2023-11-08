package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemSearchlistcontainerBinding

class SearchListContainerAdapter(val searchList: ArrayList<ArrayList<ExpenseList>>): RecyclerView.Adapter<SearchListContainerAdapter.SearchListContainerHolder>() {
    lateinit var context: Context

    inner class SearchListContainerHolder(val binding: ListitemSearchlistcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<ExpenseList>, context: Context, list: ArrayList<ArrayList<ExpenseList>>) {
            binding.daytext.isGone = true

            val adapter = SearchListAdapter(item)
            val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            binding.searchlist.layoutManager = manager
            binding.searchlist.adapter = adapter
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