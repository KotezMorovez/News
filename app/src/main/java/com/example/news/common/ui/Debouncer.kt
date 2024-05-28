package com.example.news.common.ui

import android.os.Looper
import android.os.Message
import android.os.Handler

class Debouncer<T>(
    private val listener: (value: T) -> Unit
){
    private val MESSAGE_ID = 1
    private val DELAY = 500L

    private val handler: Handler = Handler(Looper.getMainLooper()) { message ->
        if (message.what == MESSAGE_ID) {
            listener.invoke(message.obj as T)
        }
        true
    }

    fun updateValue(value: T) {
        val message = Message.obtain(handler, MESSAGE_ID, value)
        handler.removeMessages(MESSAGE_ID)
        handler.sendMessageDelayed(message, DELAY)
    }
}