package com.example.mymate

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemBankBinding
import kotlin.math.abs

class OnboardingAccountAdapter(val imgList: ArrayList<Drawable>, val nameList: ArrayList<String>): RecyclerView.Adapter<OnboardingAccountAdapter.AccountHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(img: Drawable, name: String, position: Int)
    }

    inner class AccountHolder(val binding: ListitemBankBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(img: Drawable, name: String) {
            binding.bankImage.setImageDrawable(img)
            binding.bankName.text = name.substring(0 until name.length - 2)

            if (onItemClickListener != null) {
                binding.bankImageContainer.setOnClickListener {
                    onItemClickListener?.onItemClick(img, name, absoluteAdapterPosition)
                }

                binding.bankName.setOnClickListener {
                    onItemClickListener?.onItemClick(img, name, absoluteAdapterPosition)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val binding = ListitemBankBinding.inflate(LayoutInflater.from(parent.context))
        return AccountHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val img = imgList[position]
        val name = nameList[position]
        holder.bind(img, name)
    }
}