package com.example.news.ui.homepage.adapter.item

import com.example.news.ui.common.IDelegateAdapterItem
import java.util.UUID

class NewsEndingItem(val results: Int) : IDelegateAdapterItem {
    override fun id(): String {
        return UUID.randomUUID().toString()
    }

    override fun content(): Any {
        return results
    }
}