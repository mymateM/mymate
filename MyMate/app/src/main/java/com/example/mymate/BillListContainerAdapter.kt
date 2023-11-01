package com.example.mymate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBillcontainerBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BillListContainerAdapter(val billList: ArrayList<ArrayList<bill>>, val category: String): RecyclerView.Adapter<BillListContainerAdapter.BillListContainerHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    lateinit var manager: RecyclerView.LayoutManager
    lateinit var context: Context
    //val adapter = BillListAdapter(billList)

    interface OnItemClickListener {
        fun onItemClick(item: billListItem, position: Int)
    }

    inner class BillListContainerHolder(val binding: ListitemBillcontainerBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArrayList<bill>, context: Context, list: ArrayList<ArrayList<bill>>) {
            
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy")
            val thisyear = item[0].bill_payment_date.split("-")
            val realyear = today.format(formatter)
            
            if (realyear == thisyear[0]) {
                binding.yearcomponent.isGone = true
            } else {
                binding.yeartext.text = thisyear[0] + "년"
            }

            val billlist = binding.billList
            val adapter = BillListAdapter(item, category)
            billlist.layoutManager = manager
            billlist.adapter = adapter.apply {
                setOnItemClickListener(object : BillListAdapter.OnItemClickListener {
                    override fun onItemClick(item: bill, position: Int) {
                    }
                })
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillListContainerHolder {
        val binding = ListitemBillcontainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        manager = LinearLayoutManager(parent.context)
        context = parent.context
        return  BillListContainerHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    override fun onBindViewHolder(holder: BillListContainerHolder, position: Int) {
        var item = billList[position]
        holder.bind(item, context, billList)
    }
}