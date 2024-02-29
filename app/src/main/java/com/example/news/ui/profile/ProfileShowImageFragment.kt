package com.example.news.ui.profile

import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.news.R
import com.example.news.databinding.FragmentProfileShowImageBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.loadImage

class ProfileShowImageFragment : BaseFragment<FragmentProfileShowImageBinding>() {

    override fun createViewBinding(): FragmentProfileShowImageBinding {
        return FragmentProfileShowImageBinding.inflate(layoutInflater)
    }

    override fun initUi(isFirstLaunch: Boolean) {
        val imageUrl = arguments?.getString("imageUrl")

        with(viewBinding) {
            if (imageUrl != null) {
                profileImageView.loadImage(imageUrl, R.drawable.avatar_placeholder) {
                    profileImageProgress.visibility = GONE
                }
            } else {
                profileImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.avatar_placeholder,
                        null
                    )
                )
                profileImageProgress.visibility = GONE
            }

            (activity as AppCompatActivity).setSupportActionBar(profileImageToolbar)
            profileImageToolbar.title = null
            profileImageToolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun observeData() {}
}