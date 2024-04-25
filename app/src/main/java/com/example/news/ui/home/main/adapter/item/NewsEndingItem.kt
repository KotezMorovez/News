package com.example.news.ui.home.main.adapter.item

import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem
import java.util.UUID

class NewsEndingItem(val results: Int) : IDelegateAdapterItem {
    override fun id(): String {
        return UUID.randomUUID().toString()
    }

    override fun content(): Any {
        return results
    }
}