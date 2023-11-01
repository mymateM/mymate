package com.example.mymate

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ListitemCategorylistBinding

class CategoryAdapter(val context: Context, val imgList: ArrayList<Drawable>, val nameList: ArrayList<String>, var tag: ArrayList<Boolean>, val selectedimgList: ArrayList<Drawable>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class CategoryViewHolder(val binding: ListitemCategorylistBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(img: Drawable, inputname: String, thetag: ArrayList<Boolean>, selectedimg: Drawable) {
            val icon = binding.categoryicon
            val name = binding.categoryname
            val background = binding.categoryimgbackground

            if (!thetag[absoluteAdapterPosition]) {
                icon.setImageDrawable(img)
                background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtndefault))
            } else {
                icon.setImageDrawable(selectedimg)
                background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtnselected))
            }
            name.text = inputname

            if (onItemClickListener != null) {
                binding.categoryitem.setOnClickListener {
                    onItemClickListener?.onItemClick(absoluteAdapterPosition)
                    if (!thetag[absoluteAdapterPosition]) {
                        icon.setImageDrawable(selectedimg)
                        background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtnselected))
                        thetag[absoluteAdapterPosition] = true
                    } else {
                        icon.setImageDrawable(img)
                        background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_circlebtndefault))
                        thetag[absoluteAdapterPosition] = false
                    }

                    notifyItemChanged(absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ListitemCategorylistBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val img = imgList[position]
        val text = nameList[position]
        var thetag = tag
        val selimg = selectedimgList[position]

        holder.bind(img, text, thetag, selimg)
    }
}