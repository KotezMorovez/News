package com.example.news.ui.homepage.show_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.news.R
import com.example.news.databinding.ItemHomePageShowImageCarouselBinding
import com.example.news.ui.common.setImageWithProgressbar

class HomePageShowImageCarouselStateAdapter(
    private val imageUrlList: Array<String>
) : RecyclerView.Adapter<HomePageShowImageCarouselStateAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePageShowImageCarouselStateAdapter.ViewPagerViewHolder =
        ViewPagerViewHolder(
            ItemHomePageShowImageCarouselBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(imageUrlList[position])
    }

    override fun getItemCount(): Int = imageUrlList.size

    inner class ViewPagerViewHolder(private val binding: ItemHomePageShowImageCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUrl: String) {
            binding.carouselImageView.setImageWithProgressbar(
                binding.root.context,
                binding.carouselImageView,
                imageUrl,
                180f,
                10f
            )
        }
    }
}
