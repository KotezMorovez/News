package com.example.news.ui.homepage.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.news.R
import com.example.news.databinding.ItemCarouselImageBinding

class HomePageCarouselStateAdapter(
    private val imageUrlList: List<String>,
    private val onImageItemClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<HomePageCarouselStateAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
        ViewPagerViewHolder(
            ItemCarouselImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = imageUrlList.size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(imageUrlList[position])
        holder.binding.carouselImageView.setOnClickListener {
            onImageItemClickListener.invoke(position)
        }
    }

    inner class ViewPagerViewHolder(val binding: ItemCarouselImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_homepage_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.carouselImageView)
        }
    }
}