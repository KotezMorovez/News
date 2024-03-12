package com.example.news.ui.common

interface IDelegateAdapterItem {
    fun id(): String
    fun content(): Any
    fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None : Payloadable
    }
}