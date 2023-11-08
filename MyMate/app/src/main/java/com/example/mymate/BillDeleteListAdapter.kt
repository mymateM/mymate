package com.example.mymate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBillBinding

class BillDeleteListAdapter(val billList: ArrayList<bill>, val category: String, val values: BillListValues, val checkall: Boolean): RecyclerView.Adapter<BillDeleteListAdapter.BillDeleteListHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: bill, position: Int)
    }

    inner class BillDeleteListHolder(val binding: ListitemBillBinding, val context: Context, val category: String): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: bill, category: String) {
            val itemimage = binding.billimage
            val itemday = binding.billdate
            val itemamount = binding.billamount

            if (category == "도시가스") {
                itemimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_list_gas))
            } else if (category == "수도") {
                itemimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_list_water))
            } else if (category == "전기") {
                itemimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_list_electronic))
            } else if (category == "기타") {
                itemimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_list_etc))
            }

            if (checkall) {
                binding.billitem.setBackgroundResource(R.drawable.button_selectedbox)
            } else {
                for (i in 0 until values.deleteList.size) {
                    if (values.deleteList.contains(item.bill_id)) {
                        values.deleteid(item.bill_id)
                        binding.billitem.setBackgroundResource(R.drawable.button_selectbox)
                    } else {
                        values.putid(item.bill_id)
                        binding.billitem.setBackgroundResource(R.drawable.button_selectedbox)
                    }
                }
            }

            val text = item.bill_payment_date.split("-")
            itemday.text = text[1] + "월 " + text[2] + "일까지 납부"

            itemamount.text = digitprocessing(item.bill_payment_amount) + "원"

            //itemimage.setImageDrawable(item.drawable)
            //itemday.text = item.date
            //itemamount.text = item.amount

            if (onItemClickListener != null) {
                binding.billItem.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                    if (values.deleteList.contains(item.bill_id)) {
                        values.deleteid(item.bill_id)
                        binding.billitem.setBackgroundResource(R.drawable.button_selectbox)
                    } else {
                        values.putid(item.bill_id)
                        binding.billitem.setBackgroundResource(R.drawable.button_selectedbox)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillDeleteListHolder {
        val binding = ListitemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillDeleteListHolder(binding, parent.context, category)
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    override fun onBindViewHolder(holder: BillDeleteListHolder, position: Int) {
        var item = billList[position]
        holder.bind(item, category)
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