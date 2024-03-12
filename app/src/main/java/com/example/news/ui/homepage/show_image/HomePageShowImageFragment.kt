package com.example.news.ui.homepage.show_image

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.databinding.FragmentHomePageShowImageBinding
import com.example.news.ui.common.BaseFragment

class HomePageShowImageFragment : BaseFragment<FragmentHomePageShowImageBinding>() {
    private lateinit var viewModel: HomePageShowImageViewModel

    override fun createViewBinding(): FragmentHomePageShowImageBinding {
        return FragmentHomePageShowImageBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun initUi() {
        viewModel = ViewModelProvider(this)[HomePageShowImageViewModel::class.java]

        val bundleImageUrlList = arguments?.getStringArray("imageUrlList")
        val bundlePosition = arguments?.getInt("position")

        if (viewModel.isFirstLaunch && !bundleImageUrlList.isNullOrEmpty() && bundlePosition != null) {
            viewModel.imageUrlList = bundleImageUrlList
            viewModel.position = bundlePosition
        }

        viewModel.isFirstLaunch = false

        with(viewBinding) {
            homePageImageCarousel.adapter =
                HomePageShowImageCarouselStateAdapter(viewModel.imageUrlList)

            homePageImageCarousel.setCurrentItem(viewModel.position, false)

            homePageImageProgress.visibility = View.GONE

            (activity as AppCompatActivity).setSupportActionBar(homePageImageToolbar)
            homePageImageToolbar.title = ""
            homePageImageToolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            imagePositionTextView.text = "${viewModel.position + 1}/${viewModel.imageUrlList.size}"
        }
    }

    override fun observeData() {}
}