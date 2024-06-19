package com.example.news.ui.home.main.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.ItemNewsWithImageBinding
import com.example.news.ui.common.delegate_adapter.BaseDelegateAdapter
import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem
import com.example.news.ui.common.setImageWithProgressbar
import com.example.news.ui.home.main.adapter.item.NewsImageItem

class NewsImageDelegateAdapter(
    private val onFavouriteClickListener: (id: String) -> Unit,
    private val onImageClickListener: (id: String, position: Int) -> Unit
) : BaseDelegateAdapter<NewsImageItem, NewsImageDelegateAdapter.ViewHolder>(NewsImageItem::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemNewsWithImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun bindViewHolder(
        model: NewsImageItem,
        viewHolder: ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    ) {
        with(viewHolder.binding) {
            newsHeaderTextView.text = model.ui.header
            dateTextView.text = model.ui.date

            if (model.ui.body.isBlank()) {
                newsBodyTextView.visibility = View.GONE
            } else {
                newsBodyTextView.text = model.ui.body
            }

            root.setOnClickListener {
                onClickListener?.invoke(model.id())
            }

            newsImage.setImageWithProgressbar(
                root.context,
                newsImage,
                model.ui.imagesUriList?.get(0),
                60f,
                10f
            )

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
                        R.drawable.ic_recycler_favourite_inactive,
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

    inner class ViewHolder(val binding: ItemNewsWithImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}