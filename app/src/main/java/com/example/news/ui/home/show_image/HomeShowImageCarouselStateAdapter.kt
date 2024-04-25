package com.example.news.ui.home.show_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemHomePageShowImageCarouselBinding
import com.example.news.ui.common.setImageWithProgressbar

class HomeShowImageCarouselStateAdapter(
    private val imageUrlList: Array<String>
) : RecyclerView.Adapter<HomeShowImageCarouselStateAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeShowImageCarouselStateAdapter.ViewPagerViewHolder =
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
