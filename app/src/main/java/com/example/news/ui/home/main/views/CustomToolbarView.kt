package com.example.news.ui.home.main.views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news.R
import com.example.news.common.Debouncer
import com.example.news.databinding.CustomToolbarViewBinding

class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val viewBinding =
        CustomToolbarViewBinding.inflate(LayoutInflater.from(context), this, false)

    private var onProfileIconClickedListener: (() -> Unit)? = null
    private var onFavouriteIconClickedListener: (() -> Unit)? = null
    private var onSearchTextChangeListener: ((value: String) -> Unit)? = null
    private var onSearchCanceledListener: (() -> Unit)? = null

    fun onProfileIconClickedListener(listener: () -> Unit) {
        onProfileIconClickedListener = listener
    }

    fun setOnFavouriteIconClickedListener(listener: () -> Unit) {
        onFavouriteIconClickedListener = listener
    }

    fun setOnSearchTextChangeListener(listener: (value: String) -> Unit) {
        onSearchTextChangeListener = listener
    }

    fun setOnSearchCanceledListener(listener: () -> Unit) {
        onSearchCanceledListener = listener
    }

    init {
        addView(viewBinding.root)

        with(viewBinding) {
            homePageToolbarProfile.setOnClickListener {
                onProfileIconClickedListener?.invoke()
            }

            homePageToolbarFavorite.setOnClickListener {
                onFavouriteIconClickedListener?.invoke()
            }

            homePageToolbarSearch.setOnClickListener {
                showSearchField()
            }

            homePageToolbarSearchClose.setOnClickListener {
                onSearchCanceledListener?.invoke()
                homePageToolbarSearch.clearFocus()
                hideSearchField()
                homePageSearchEditText.text = null
            }

            homePageSearchEditText.addTextChangedListener { editable ->
                if (editable != null) {
                    Debouncer<Editable> (
                        listener = {
                            onSearchTextChangeListener?.invoke(editable.toString())
                        }
                    ).updateValue(editable)
                }
            }
        }
    }

    fun setImage(url: String) {
        Glide.with(viewBinding.homePageToolbarProfile)
            .load(url)
            .placeholder(R.drawable.avatar_placeholder_light)
            .circleCrop()
            .apply(RequestOptions().override(100, 100))
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