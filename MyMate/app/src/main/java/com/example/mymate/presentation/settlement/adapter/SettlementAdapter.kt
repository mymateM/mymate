package com.example.mymate.presentation.settlement.adapter

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
import com.example.mymate.R
import com.example.mymate.data.dto.report.MemberMonthlyStatus
import com.example.mymate.databinding.ListitemModaleBinding
import com.example.mymate.util.getTypefaceSpan
import java.text.DecimalFormat

class SettlementAdapter(val mateList: ArrayList<MemberMonthlyStatus>): RecyclerView.Adapter<SettlementAdapter.SettlementViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var clickposition = -1

    interface OnItemClickListener {
        fun onItemClick(item: MemberMonthlyStatus, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class SettlementViewHolder(val binding: ListitemModaleBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        val suitBoldTypeface: Typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        val montBoldTypeface: Typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        fun bind(item: MemberMonthlyStatus) {
            val nametxt = item.name + "에게"
            val billtxt = SpannableStringBuilder("${DecimalFormat("#,###").format(item.settlement_amount.toInt())}원")
            billtxt.setSpan(suitBoldTypeface.getTypefaceSpan(), billtxt.lastIndex, billtxt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                    if (clickposition != absoluteAdapterPosition) {
                        clickposition = absoluteAdapterPosition
                    } else {
                        clickposition = -1
                    }
                    notifyItemRangeChanged(0, mateList.size)
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

}