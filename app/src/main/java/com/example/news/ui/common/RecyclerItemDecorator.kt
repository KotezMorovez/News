package com.example.news.ui.common

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerItemDecorator(
    private var mDivider: Drawable
): RecyclerView.ItemDecoration() {
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.getPaddingLeft()
        val dividerRight = parent.width - parent.getPaddingRight()
        val childCount = parent.childCount

        for (i in 0..childCount - 2) {
            val child: View = parent.getChildAt(i)
            val dividerTop: Int = child.bottom + child.layoutParams.height
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }
}