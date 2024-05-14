package com.example.news.ui.home.favourite.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class FavouriteAdapterDiffCallback : DiffUtil.ItemCallback<FavouriteItem>() {
    override fun areItemsTheSame(
        oldItem: FavouriteItem,
        newItem: FavouriteItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: FavouriteItem,
        newItem: FavouriteItem
    ): Boolean {
        return (oldItem.title == newItem.title && oldItem.image == newItem.image)
    }
}