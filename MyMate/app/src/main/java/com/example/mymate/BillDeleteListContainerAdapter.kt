package com.example.mymate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBillcontainerBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BillDeleteListContainerAdapter(val billList: ArrayList<ArrayList<bill>>, val category: String, val values: BillListValues, var checkall: Boolean): RecyclerView.Adapter<BillDeleteListContainerAdapter.BillDeleteListContainerHolder>() {
    private var onItemClickListener: BillDeleteListContainerAdapter.OnItemClickListener? = null
    lateinit var manager: RecyclerView.LayoutManager
    lateinit var context: Context
    lateinit var adapter: BillDeleteListAdapter

    interface OnItemClickListener {
        fun onItemClick(item: billListItem, position: Int)
    }

    inner class BillDeleteListContainerHolder(val binding: ListitemBillcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<bill>, context: Context, list: ArrayList<ArrayList<bill>>) {

            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy")
            val theyear = list[absoluteAdapterPosition][0].bill_payment_date.split("-")
            val thisyear = item[0].bill_payment_date.split("-")
            val realyear = today.format(formatter)

            binding.strokeheader.isGone = true
            binding.stroketail.isGone = true
            binding.yeartext.isGone = true

            if (absoluteAdapterPosition != 0) {
                if (theyear[0] != realyear && theyear[0] != list[absoluteAdapterPosition - 1][0].bill_payment_date.split("-")[0]) {
                    binding.strokeheader.isGone = false
                    binding.stroketail.isGone = false
                    binding.yeartext.isGone = false
                    binding.yeartext.text = theyear[0] + "년"
                } else {
                    binding.strokeheader.isGone = true
                    binding.stroketail.isGone = true
                    binding.yeartext.isGone = true
                }
            }

            val billlist = binding.billList
            adapter = BillDeleteListAdapter(item, category, values, checkall)
            billlist.layoutManager = manager
            billlist.adapter = adapter.apply {
                setOnItemClickListener(object : BillDeleteListAdapter.OnItemClickListener {
                    override fun onItemClick(item: bill, position: Int) {
                    }
                })
            }
        }
    }

    fun setOnItemClickListener(listener: BillDeleteListContainerAdapter.OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillDeleteListContainerHolder {
        val binding = ListitemBillcontainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        manager = LinearLayoutManager(parent.context)
        context = parent.context
        return  BillDeleteListContainerHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    override fun onBindViewHolder(holder: BillDeleteListContainerHolder, position: Int) {
        var item = billList[position]
        holder.bind(item, context, billList)
    }
}