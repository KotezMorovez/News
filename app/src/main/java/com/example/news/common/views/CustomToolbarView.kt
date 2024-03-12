package com.example.news.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news.R
import com.example.news.databinding.CustomToolbarViewBinding

class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val viewBinding =
        CustomToolbarViewBinding.inflate(LayoutInflater.from(context), this, false)

    private var goToProfile: (() -> Unit)? = null
    private var goToFavourite: (() -> Unit)? = null

    fun setGoToProfileListener(listener: () -> Unit) {
        goToProfile = listener
    }

    fun setGoToFavouriteListener(listener: () -> Unit) {
        goToFavourite = listener
    }

    init {
        addView(viewBinding.root)

        with(viewBinding) {
            homePageToolbarProfile.setOnClickListener {
                goToProfile?.invoke()
            }

            homePageToolbarFavorite.setOnClickListener {
                goToFavourite?.invoke()
            }

            homePageToolbarSearch.setOnClickListener {
                showSearchField()
            }

            homePageToolbarSearchClose.setOnClickListener {
                hideSearchField()
            }
        }
    }

    fun setImage(url: String) {
        Glide.with(viewBinding.homePageToolbarProfile)
            .load(url)
            .placeholder(R.drawable.avatar_placeholder)
            .circleCrop()
            .apply(RequestOptions().override(50, 50))
            .into(viewBinding.homePageToolbarProfile)
    }

    private fun showSearchField() {
        with(viewBinding) {
            mainContainer.animate().alpha(0.0f).duration = 300L
            mainContainer.visibility = GONE

            searchContainer.animate().alpha(1.0f).duration = 300L
            searchContainer.visibility = VISIBLE
        }
    }

    private fun hideSearchField() {
        with(viewBinding) {
            mainContainer.animate().alpha(1.0f).duration = 300L
            mainContainer.visibility = VISIBLE

            searchContainer.animate().alpha(0.0f).duration = 300L
            searchContainer.visibility = GONE
        }
    }
}