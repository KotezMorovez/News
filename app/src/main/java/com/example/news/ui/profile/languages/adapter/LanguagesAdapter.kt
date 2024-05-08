package com.example.news.ui.profile.languages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemLanguagesBinding
import com.example.news.databinding.ItemSourcesBinding
import com.example.news.ui.profile.sources.adapter.SourcesAdapter

class LanguagesAdapter:
    ListAdapter<LanguagesItem, LanguagesAdapter.ViewHolder>(LanguagesAdapterDiffCallback()) {
    private lateinit var onCheckBoxClickListener: (id: String, isUse: Boolean) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLanguagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            languageTextView.text = item.id
            languageRadioButton.isChecked = item.isCheck
            languageRadioButton.setOnCheckedChangeListener { _, isChecked ->
                onCheckBoxClickListener.invoke(item.id, isChecked)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.binding.languageRadioButton.setOnCheckedChangeListener(null)
        super.onViewRecycled(holder)
    }
    fun setOnCheckBoxClickListener(onCheckBoxClickListener: (id: String, isUse: Boolean) -> Unit) {
        this.onCheckBoxClickListener = onCheckBoxClickListener
    }

    inner class ViewHolder(val binding: ItemLanguagesBinding) : RecyclerView.ViewHolder(binding.root)
}