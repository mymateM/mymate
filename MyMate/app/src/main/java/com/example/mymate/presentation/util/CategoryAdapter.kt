package com.example.mymate.presentation.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.databinding.ListitemCategorylistBinding
import com.example.mymate.util.Category
import com.example.mymate.util.CategoryGrayIconProvider
import com.example.mymate.util.CategoryPurpleIconProvider

class CategoryAdapter(val context: Context, var tag: ArrayList<Boolean>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class CategoryViewHolder(val binding: ListitemCategorylistBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(flag: ArrayList<Boolean>) {
            val category = Category.fromIndex(absoluteAdapterPosition)
            val icon = binding.categoryicon
            val name = binding.categoryname
            val background = binding.categoryimgbackground

            name.text = category.displayName
            if (!flag[absoluteAdapterPosition]) {
                icon.setImageDrawable(CategoryGrayIconProvider.getIconImage(context, category))
                background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtndefault))
            } else {
                icon.setImageDrawable(CategoryPurpleIconProvider.getIconImage(context, category))
                background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtnselected))
            }

            binding.categoryitem.setOnClickListener {
                onItemClickListener?.onItemClick(absoluteAdapterPosition)
                if (flag[absoluteAdapterPosition]) {
                    flag.replaceAll { false }
                } else {
                    flag.replaceAll { false }
                    flag[absoluteAdapterPosition] = true
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ListitemCategorylistBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tag.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var flag = tag

        holder.bind(flag)
    }
}