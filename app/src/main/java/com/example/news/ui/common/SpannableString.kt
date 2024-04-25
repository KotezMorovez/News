package com.example.news.ui.common

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.example.news.R

fun makeLinks(
    text: String,
    phrase: String,
    context: Context,
    listener: View.OnClickListener
): SpannableString {
    val spannableString = SpannableString(text)
    val start = text.indexOf(phrase)
    val end = start + phrase.length
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = context.resources.getColor(R.color.white, null)
            ds.isUnderlineText = false
        }
        override fun onClick(v: View) {
            listener.onClick(v)
        }
    }
    spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableString
}
