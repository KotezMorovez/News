package com.example.news.ui.common

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.Placeholder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.ResourceCallback
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.news.R

fun ImageView.loadImage(url: String, @DrawableRes placeholder: Int, callback: ()->Unit) {
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        .dontTransform()

    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .apply(RequestOptions().apply(options))
        .dontAnimate()
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                this@loadImage.setImageDrawable(resource)
                callback.invoke()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                this@loadImage.setImageDrawable(placeholder)
                callback.invoke()
            }
        })
}