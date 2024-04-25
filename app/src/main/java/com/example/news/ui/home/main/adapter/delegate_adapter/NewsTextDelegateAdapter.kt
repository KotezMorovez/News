package com.example.news.ui.home.main.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.ItemNewsWithoutImageBinding
import com.example.news.ui.common.delegate_adapter.BaseDelegateAdapter
import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem
import com.example.news.ui.home.main.adapter.item.NewsTextItem

class NewsTextDelegateAdapter(
    private val onFavouriteClickListener: (id: String) -> Unit
) : BaseDelegateAdapter<NewsTextItem, NewsTextDelegateAdapter.ViewHolder>(NewsTextItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemNewsWithoutImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun bindViewHolder(
        model: NewsTextItem,
        viewHolder: ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    ) {
        with(viewHolder.binding) {
            newsHeaderTextView.text = model.ui.header
            newsBodyTextView.text = model.ui.body

            root.setOnClickListener {
                onClickListener?.invoke(model.id())
            }

            addToFavoritesButton.setImageDrawable(
                ResourcesCompat.getDrawable(
                    this.root.context.resources,
                    if (model.ui.isFavorite) R.drawable.ic_favourite_active_ripple else R.drawable.ic_recycler_favourite_inactive_24,
                    null
                )
            )

            addToFavoritesButton.setOnClickListener {
                onFavouriteClickListener.invoke(model.ui.id)
            }

            dataTextView.text = model.ui.date.toString()
        }
    }

    inner class ViewHolder(val binding: ItemNewsWithoutImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}