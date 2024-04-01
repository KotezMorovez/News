package com.example.news.ui.homepage.adapter.delegate_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemCarouselImageBinding
import com.example.news.ui.common.setImageWithProgressbar

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
            binding.carouselImageView.setImageWithProgressbar(
                binding.root.context,
                binding.carouselImageView,
                imageUrl,
                60f,
                10f
            )
        }
    }
}