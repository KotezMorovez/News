package com.example.news.ui.homepage.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.example.news.R
import com.example.news.databinding.ItemNewsWithCarouselBinding
import com.example.news.ui.common.BaseDelegateAdapter
import com.example.news.ui.common.IDelegateAdapterItem
import com.example.news.ui.homepage.adapter.item.NewsCarouselItem

class NewsCarouselDelegateAdapter(
    private val onFavouriteClickListener: (id: String) -> Unit,
    private val onImageClickListener: (id: String, position: Int) -> Unit
) : BaseDelegateAdapter<NewsCarouselItem, NewsCarouselDelegateAdapter.ViewHolder>(NewsCarouselItem:: class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = ViewHolder(ItemNewsWithCarouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
        PagerSnapHelper().attachToRecyclerView(holder.binding.imageCarousel)
        return holder
    }



    override fun bindViewHolder(
        model: NewsCarouselItem,
        viewHolder: NewsCarouselDelegateAdapter.ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    ) {
        with(viewHolder.binding){
            newsHeaderTextView.text = model.ui.header
            newsBodyTextView.text = model.ui.body


            imageCarousel.adapter = HomePageCarouselStateAdapter(
                model.ui.imagesUriList,
                onImageItemClickListener = {
                    onImageClickListener.invoke(model.ui.id, it)
                }
            )
            imageCarousel.layoutManager = LinearLayoutManager(
                imageCarousel.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            dotsCarousel.removeAllTabs()
            model.ui.imagesUriList.forEach { dotsCarousel.addTab(dotsCarousel.newTab()) }

            TabbedListMediator(
                imageCarousel,
                dotsCarousel,
                model.ui.imagesUriList.indices.toList(),
                true
            ).attach()

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

            dataTextView.text = model.ui.data
        }
    }

    inner class ViewHolder(val binding: ItemNewsWithCarouselBinding) : RecyclerView.ViewHolder(binding.root)
}