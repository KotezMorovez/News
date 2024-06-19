package com.example.news.ui.common.delegate_adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDelegateAdapter<M, in ViewHolder : RecyclerView.ViewHolder>(
    val modelClass: Class<out M>
) {
    protected var onClickListener: ((String) -> Unit)? = null
        private set

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindViewHolder(
        model: M,
        viewHolder: ViewHolder,
        payloads: List<IDelegateAdapterItem.Payloadable>
    )

    open fun onViewRecycled(viewHolder: ViewHolder) = Unit
    open fun onViewDetachedFromWindow(viewHolder: ViewHolder) = Unit
    open fun onViewAttachedToWindow(viewHolder: ViewHolder) = Unit

    fun setOnClickListener(listener: ((String) -> Unit)?) {
        this.onClickListener = listener
    }
}