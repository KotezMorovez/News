package com.example.news.ui.profile.languages.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class LanguagesAdapterDiffCallback: DiffUtil.ItemCallback<LanguagesItem>() {
    override fun areItemsTheSame(
        oldItem: LanguagesItem,
        newItem: LanguagesItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: LanguagesItem,
        newItem: LanguagesItem
    ): Boolean {
        return (oldItem.isCheck == newItem.isCheck)
    }
}