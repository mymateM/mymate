package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBillBinding

class BillListAdapter(val billList: ArrayList<bill>, val category: String): RecyclerView.Adapter<BillListAdapter.BillListHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: bill, position: Int)
    }

    inner class BillListHolder(val binding: ListitemBillBinding, val context: Context, val category: String): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.P)
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

            val text = item.bill_payment_date.split("-")
            itemday.text = text[1] + "월 " + text[2] + "일까지 납부"
            val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
            val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
            val montSemiboldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_semibold), Typeface.NORMAL)
            val suitSemiboldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_semibold), Typeface.NORMAL)
            val amount = SpannableStringBuilder("${digitprocessing(item.bill_payment_amount)}원")
            amount.setSpan(TypefaceSpan(montBoldTypeface), 0, amount.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            amount.setSpan(TypefaceSpan(suitSemiboldTypeface), amount.length - 1, amount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemamount.text = amount

            //itemimage.setImageDrawable(item.drawable)
            //itemday.text = item.date
            //itemamount.text = item.amount

            if (onItemClickListener != null) {
                binding.billItem.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                    var detailIntent = Intent(context, BillDetailActivity::class.java)
                    detailIntent.putExtra("itemid", item.bill_id)
                    detailIntent.putExtra("category", category)
                    context.startActivity(detailIntent)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillListHolder {
        val binding = ListitemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillListHolder(binding, parent.context, category)
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: BillListHolder, position: Int) {
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