package com.example.news.ui.homepage.adapter.item

import com.example.news.ui.common.IDelegateAdapterItem

class NewsCarouselItem(val ui: NewsUi) : IDelegateAdapterItem {
    override fun id(): String {
        return ui.id
    }

    override fun content(): NewsUi {
        return ui
    }
}