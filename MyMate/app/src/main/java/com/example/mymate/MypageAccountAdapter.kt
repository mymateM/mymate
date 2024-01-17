package com.example.mymate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemMypageaccountBinding

class MypageAccountAdapter(val members: ArrayList<mateAccount>): RecyclerView.Adapter<MypageAccountAdapter.AccountHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: mateAccount, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class AccountHolder(val binding: ListitemMypageaccountBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: mateAccount) {
            binding.accountNumber.text = item.account_bank.replace("은행", "") + " " + item.account_number
            binding.accountOwner.text = item.user_name
            when (item.user_profile_image) {
                "img1" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile1))
                "img2" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile2))
                "img3" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile3))
                "img4" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile4))
                "img5" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile5))
                "img6" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile6))
                "img7" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile7))
                "img8" -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile8))
                else -> binding.accountProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile1))
            }

            if (onItemClickListener != null) {
                binding.copypaste.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                }

                binding.accountNumber.setOnClickListener {
                    onItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val binding = ListitemMypageaccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val item = members[position]
        holder.bind(item)
    }

}