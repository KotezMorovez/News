package com.example.news.ui.profile.sources.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class SourcesAdapterDiffCallback : DiffUtil.ItemCallback<SourcesItem>() {
    override fun areItemsTheSame(
        oldItem: SourcesItem,
        newItem: SourcesItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: SourcesItem,
        newItem: SourcesItem
    ): Boolean {
        return (oldItem.text == newItem.text && oldItem.isUse == newItem.isUse)
    }
}