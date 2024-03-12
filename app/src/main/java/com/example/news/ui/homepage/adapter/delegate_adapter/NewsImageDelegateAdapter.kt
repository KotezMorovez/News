package com.example.news.ui.homepage.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ItemNewsWithImageBinding
import com.example.news.ui.common.BaseDelegateAdapter
import com.example.news.ui.common.IDelegateAdapterItem
import com.example.news.ui.homepage.adapter.item.NewsImageItem

class NewsImageDelegateAdapter(
    private val onFavouriteClickListener: (id: String) -> Unit,
    private val onImageClickListener: (id: String, position: Int) -> Unit
) : BaseDelegateAdapter<NewsImageItem, NewsImageDelegateAdapter.ViewHolder>(NewsImageItem:: class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(ItemNewsWithImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun bindViewHolder(
        model: NewsImageItem,
        viewHolder: ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    ) {
        with(viewHolder.binding){
            newsHeaderTextView.text = model.ui.header
            dataTextView.text = model.ui.data
            newsBodyTextView.text = model.ui.body

            Glide.with(newsImage)
                .load(model.ui.imagesUriList[0])
                .placeholder(R.drawable.ic_homepage_placeholder)
                .into(newsImage)

            if (model.ui.isFavorite) {
                addToFavoritesButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        addToFavoritesButton.context.resources,
                        R.drawable.ic_favourite_active_ripple,
                        null
                    )
                )
            } else {
                addToFavoritesButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        addToFavoritesButton.context.resources,
                        R.drawable.ic_recycler_favourite_inactive_24,
                        null
                    )
                )
            }

            addToFavoritesButton.setOnClickListener {
                onFavouriteClickListener.invoke(model.ui.id)
            }

            newsImage.setOnClickListener {
                onImageClickListener.invoke(model.ui.id, 0)
            }
        }
    }

    inner class ViewHolder(val binding: ItemNewsWithImageBinding) : RecyclerView.ViewHolder(binding.root)
}