package com.example.news.ui.home.main.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.ItemNewsEndingBinding
import com.example.news.ui.common.delegate_adapter.BaseDelegateAdapter
import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem
import com.example.news.ui.home.main.adapter.item.NewsEndingItem

class NewsEndingDelegateAdapter() : BaseDelegateAdapter<NewsEndingItem, NewsEndingDelegateAdapter.ViewHolder>(
    NewsEndingItem::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(ItemNewsEndingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun bindViewHolder(
        model: NewsEndingItem,
        viewHolder: ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    ) {
        with(viewHolder.binding) {
            newsEndingTextView.text = root.context.resources.getString(
                R.string.home_page_ending_item_text,
                model.results
            )
        }
    }

    inner class ViewHolder(val binding: ItemNewsEndingBinding) : RecyclerView.ViewHolder(binding.root)
}