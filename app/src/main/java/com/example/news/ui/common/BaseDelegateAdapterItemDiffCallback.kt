package com.example.news.ui.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class BaseDelegateAdapterItemDiffCallback: DiffUtil.ItemCallback<IDelegateAdapterItem>() {
    override fun areItemsTheSame(oldItem: IDelegateAdapterItem, newItem: IDelegateAdapterItem): Boolean {
        return oldItem.id() == newItem.id()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IDelegateAdapterItem, newItem: IDelegateAdapterItem): Boolean {
        return oldItem.content() == newItem.content()
    }

    override fun getChangePayload(oldItem: IDelegateAdapterItem, newItem: IDelegateAdapterItem): Any {
        return oldItem.payload(newItem)
    }
}