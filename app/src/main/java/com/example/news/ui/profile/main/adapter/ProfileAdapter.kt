package com.example.news.ui.profile.main.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemProfileInfoBinding

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private var items: List<ProfileInfoItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<ProfileInfoItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemProfileInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = items[position]
            profileInfoText.text = item.text
            if (item.isBold) {
                profileInfoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
                profileInfoText.setTypeface(null, Typeface.BOLD)
            }
        }
    }

    inner class ViewHolder(val binding: ItemProfileInfoBinding) : RecyclerView.ViewHolder(binding.root)
}

