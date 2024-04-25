package com.example.news.ui.home.main.adapter.item

import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem

class NewsCarouselItem(val ui: NewsUi) : IDelegateAdapterItem {
    override fun id(): String {
        return ui.id
    }

    override fun content(): NewsUi {
        return ui
    }
}