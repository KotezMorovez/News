package com.example.news.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
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

fun ImageView.loadThumbNailImage(url: String, @DrawableRes placeholder: Int, callback: ()->Unit){
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .override(50, 50)
        .into(this)
}

fun ImageView.setImageWithProgressbar(
    context: Context,
    imageView: ImageView,
    load: String?,
    placeholderRadius: Float,
    placeholderWidth: Float
){
    val glideFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    val placeholder = CircularProgressDrawable(context)
    placeholder.strokeWidth= placeholderWidth
    placeholder.centerRadius = placeholderRadius
    placeholder.setColorSchemeColors(
        context.resources.getColor(R.color.blue_500, null),
        context.resources.getColor(R.color.blue_600, null),
        context.resources.getColor(R.color.blue_700, null),
        context.resources.getColor(R.color.blue_800, null)
    )
    placeholder.start()

    Glide.with(imageView)
        .load(load)
        .transition(DrawableTransitionOptions.withCrossFade(glideFactory))
        .placeholder(placeholder)
        .into(imageView)
}