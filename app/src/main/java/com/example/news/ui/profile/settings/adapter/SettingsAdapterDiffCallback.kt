package com.example.news.ui.profile.settings.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class SettingsAdapterDiffCallback: DiffUtil.ItemCallback<SettingsSourceItem>() {
    override fun areItemsTheSame(
        oldItem: SettingsSourceItem,
        newItem: SettingsSourceItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: SettingsSourceItem,
        newItem: SettingsSourceItem
    ): Boolean {
        return (oldItem.text == newItem.text && oldItem.isUse == newItem.isUse)
    }
}