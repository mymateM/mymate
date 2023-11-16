package com.example.mymate

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemModaleBinding

class SettlementAdapter(val mateList: ArrayList<mateSettleInfo>): RecyclerView.Adapter<SettlementAdapter.SettlementViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var clickposition = -1

    interface OnItemClickListener {
        fun onItemClick(item: mateSettleInfo, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class SettlementViewHolder(val binding: ListitemModaleBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(item: mateSettleInfo) {
            val nametxt = item.name + "에게"
            val billtxt = SpannableStringBuilder("${digitprocessing(item.settlement_amount)}원")
            val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
            billtxt.setSpan(TypefaceSpan(suitBoldTypeface), billtxt.lastIndex - 1, billtxt.lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.modalelistname.text = nametxt
            binding.modalelistbill.text = billtxt

            if (absoluteAdapterPosition == clickposition) {
                binding.modaleunit.background = ContextCompat.getDrawable(context, R.drawable.button_selectedbox)
            } else {
                binding.modaleunit.background = ContextCompat.getDrawable(context, R.drawable.button_selectbox)
            }

            if (onItemClickListener != null) {
                binding.modaleunit.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                    if (clickposition == -1) {
                        clickposition = absoluteAdapterPosition
                        notifyItemRangeChanged(0, mateList.size)
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettlementViewHolder {
        val binding = ListitemModaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettlementViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return mateList.size
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: SettlementViewHolder, position: Int) {
        val item = mateList[position]
        holder.bind(item)
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