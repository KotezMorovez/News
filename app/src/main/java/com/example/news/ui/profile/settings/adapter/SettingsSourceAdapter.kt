package com.example.news.ui.profile.settings.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemSettingsBinding

class SettingsSourceAdapter :
    ListAdapter<SettingsSourceItem, SettingsSourceAdapter.ViewHolder>(SettingsAdapterDiffCallback()) {
    private lateinit var onCheckBoxClickListener: (id: String, isUse: Boolean) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSettingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            sourceTextView.text = item.text
            sourceCheckBox.isChecked = item.isUse
            sourceCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckBoxClickListener.invoke(item.id, isChecked)
            }
        }
    }

/**
 * При переиспользовании ViewHolder, во время установки актуального состояния checkBox срабатывает setOnCheckedChangeListener из-за чего данные в RecyclerView отображаются некорректно. Решение: в методе onViewRecycled, который вызывается при переиспользовании ViewHolder, onCheckedChangeListener присваиваем null.
 */
    override fun onViewRecycled(holder: ViewHolder) {
        holder.binding.sourceCheckBox.setOnCheckedChangeListener(null)
        super.onViewRecycled(holder)
    }

    fun setOnCheckBoxClickListener(onCheckBoxClickListener: (id: String, isUse: Boolean) -> Unit) {
        this.onCheckBoxClickListener = onCheckBoxClickListener
    }

    inner class ViewHolder(val binding: ItemSettingsBinding) : RecyclerView.ViewHolder(binding.root)
}

