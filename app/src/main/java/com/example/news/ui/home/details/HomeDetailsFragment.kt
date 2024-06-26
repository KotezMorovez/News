package com.example.news.ui.home.details

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentHomeDetailsBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.parcelable
import com.example.news.ui.common.setImageWithProgressbar
import com.example.news.ui.home.models.DetailsUi
import javax.inject.Inject

class HomeDetailsFragment : BaseFragment<FragmentHomeDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<HomeDetailsViewModel>

    private val viewModel: HomeDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeDetailsViewModel::class.java]
    }

    private var news: DetailsUi? = null

    override fun createViewBinding(): FragmentHomeDetailsBinding {
        return FragmentHomeDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        news = arguments?.parcelable("item")
        viewModel.setDetails(news)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            if (news?.header?.isEmpty() == true) {
                header.isVisible = false
            }

            if (news?.body?.isEmpty() == true) {
                body.isVisible = false
            }

            link.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            link.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link.text.toString()));
                startActivity(browserIntent)
            }

            addToFavoritesButton.setOnClickListener {
                viewModel.handleFavouriteClick()
            }
        }
    }

    override fun observeData() {
        viewModel.newsDetails.observe(viewLifecycleOwner) { news ->
            with(viewBinding) {
                header.text = news.header
                body.text = news.body
                link.text = news.url
                date.text = news.date

                if (news.imagesUriList != null) {
                    newsImage.setImageWithProgressbar(
                        root.context,
                        newsImage,
                        news.imagesUriList[0],
                        60f,
                        10f
                    )
                    newsImage.setOnClickListener {
                        viewModel.handleShowImageClick(0)
                    }
                } else {
                    newsImage.visibility = GONE
                }

                if (news.isFavorite) {
                    addToFavoritesButton.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            addToFavoritesButton.context.resources,
                            R.drawable.ic_favourite_active_ripple,
                            null
                        )
                    )
                } else {
                    addToFavoritesButton.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            addToFavoritesButton.context.resources,
                            R.drawable.ic_recycler_favourite_inactive,
                            null
                        )
                    )
                }
            }
        }

        viewModel.goToShowImageEvent.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putStringArray("imageUrlList", it.list.toTypedArray())
            bundle.putInt("position", it.position)

            this@HomeDetailsFragment.findNavController()
                .navigate(R.id.action_homeDetailsFragment_to_homeShowImageFragment, bundle)
        }
    }
}