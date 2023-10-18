package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBillBinding

class BillListAdapter(val billList: ArrayList<billListItem>): RecyclerView.Adapter<BillListAdapter.BillListHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: billListItem, position: Int)
    }

    inner class BillListHolder(val binding: ListitemBillBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: billListItem) {
            val itemimage = binding.billimage
            val itemday = binding.billdate
            val itemamount = binding.billamount
            val itemname = binding.billname

            itemimage.setImageDrawable(item.drawable)
            itemday.text = item.date
            itemamount.text = item.amount
            itemname.text = item.name

            if (onItemClickListener != null) {
                binding.billItem.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                    //TODO: StartActivity -> detail info
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillListHolder {
        val binding = ListitemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillListHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    override fun onBindViewHolder(holder: BillListHolder, position: Int) {
        var item = billList[position]
        holder.bind(item)
    }

}