package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype.databinding.SettlementModaleListBinding

class ModaleAdapter(val context: Context, val listItem: ArrayList<room_mate>): RecyclerView.Adapter<ModaleAdapter.ModaleViewHolder>() {

    var selectedposition = -1
    var selectedLayout: ConstraintLayout? = null
    var thisnum = -1
    var selected = false
    var listener: OnSendStateInterface? = null

    interface OnSendStateInterface {
        fun sendValue(position: Int)
    }

    inner class ModaleViewHolder(val binding: SettlementModaleListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: room_mate) {

            val name = binding.modalelistname
            val bill = binding.modalelistbill

            name.text = item.user_name
            var totaltext = item.user_settlement_ratio
            var textlength = totaltext.length
            var totalexpense = ""
            while (0 < textlength) {
                var substring1 = ""
                if (textlength == 3) {
                    if (totalexpense == "") {
                        totalexpense = totaltext.substring(0 until 3)
                    } else {
                        totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                    }
                } else if (textlength > 3) {
                    substring1 = totaltext.substring(textlength - 3 until textlength)
                    if (totalexpense == "") {
                        totalexpense = substring1
                    } else {
                        totalexpense = substring1 + "," + totalexpense
                    }
                } else if (textlength < 3) {
                    substring1 = totaltext.substring(0 until textlength)
                    totalexpense = substring1 + "," + totalexpense
                }

                textlength = textlength - 3
            }
            bill.text = totalexpense

            if (absoluteAdapterPosition == selectedposition) {
                binding.modaleunit.setBackgroundResource(R.drawable.settlementbackground)
            } else {
                binding.modaleunit.setBackgroundResource(R.drawable.settlementstroke)
            }

            binding.modaleunit.setOnClickListener {
                if (selectedposition == absoluteAdapterPosition) {
                    selectedposition = -1
                    binding.modaleunit.setBackgroundResource(R.drawable.settlementstroke)
                } else {
                    if (selectedposition >= 0 || selectedLayout != null) {
                        selectedLayout?.setBackgroundResource(R.drawable.settlementstroke)
                    }

                    selectedposition = absoluteAdapterPosition
                    selectedLayout = binding.modaleunit
                    binding.modaleunit.setBackgroundResource(R.drawable.settlementbackground)
                }

                thisnum = absoluteAdapterPosition
                if (selectedposition >= 0) {
                    selected = true
                }

                listener?.sendValue(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModaleViewHolder {
        val binding = SettlementModaleListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ModaleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ModaleViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    fun setOnStateInterface(listener: OnSendStateInterface) {
        this.listener = listener
    }

}