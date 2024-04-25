package com.example.news.ui.home.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemCarouselImageBinding
import com.example.news.ui.common.setImageWithProgressbar

class HomeDetailsAdapter(
    private val onImageItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<HomeDetailsAdapter.ViewHolder>() {
    private lateinit var items: List<String>

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<String>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            ItemCarouselImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.carouselImageView.setImageWithProgressbar(
            holder.binding.root.context,
            holder.binding.carouselImageView,
            items[position],
            60f,
            10f
        )

        holder.binding.carouselImageView.setOnClickListener {
            onImageItemClickListener.invoke(position)
        }
    }

    inner class ViewHolder(val binding: ItemCarouselImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}