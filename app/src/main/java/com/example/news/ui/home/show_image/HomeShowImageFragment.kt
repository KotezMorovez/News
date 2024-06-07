package com.example.news.ui.home.show_image

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.databinding.FragmentHomeShowImageBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.common.BaseFragment
import javax.inject.Inject

class HomeShowImageFragment : BaseFragment<FragmentHomeShowImageBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<HomeShowImageViewModel>

    private val viewModel: HomeShowImageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeShowImageViewModel::class.java]
    }

    override fun createViewBinding(): FragmentHomeShowImageBinding {
        return FragmentHomeShowImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun initUi() {
        val bundleImageUrlList = arguments?.getStringArray("imageUrlList")
        val bundlePosition = arguments?.getInt("position")

        if (viewModel.isFirstLaunch && !bundleImageUrlList.isNullOrEmpty() && bundlePosition != null) {
            viewModel.imageUrlList = bundleImageUrlList
            viewModel.position = bundlePosition
        }

        viewModel.isFirstLaunch = false

        with(viewBinding) {
            homePageImageCarousel.adapter =
                HomeShowImageCarouselStateAdapter(viewModel.imageUrlList)

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