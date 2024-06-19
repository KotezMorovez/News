package com.example.news.ui.home.favourite.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemFavouriteBinding
import com.example.news.ui.common.setImageWithProgressbar

class FavouriteAdapter(
    private val onImageClickListener: (id: String) -> Unit,
    private val onLinkClickListener: (link: String) -> Unit
) :
    ListAdapter<FavouriteItem, FavouriteAdapter.ViewHolder>(FavouriteAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFavouriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            if (item.title.isNullOrEmpty()) {
                newsHeaderTextView.visibility = GONE
            } else {
                newsHeaderTextView.text = item.title
            }

            link.text = item.url
            link.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            link.setOnClickListener {
                onLinkClickListener.invoke(item.url)
            }

            if (item.image != null) {
                newsImage.setImageWithProgressbar(
                    root.context,
                    newsImage,
                    item.image,
                    60f,
                    10f
                )
                newsImage.setOnClickListener {
                    onImageClickListener.invoke(item.image)
                }
            } else {
                newsImage.visibility = GONE
            }
        }
    }

    inner class ViewHolder(val binding: ItemFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root)
}