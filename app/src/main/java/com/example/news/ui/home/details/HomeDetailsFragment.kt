package com.example.news.ui.home.details

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentHomeDetailsBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.parcelable
import com.example.news.ui.home.models.DetailsUi

class HomeDetailsFragment : BaseFragment<FragmentHomeDetailsBinding>() {
    private lateinit var viewModel: HomeDetailsViewModel
    private lateinit var viewModelFactory: HomeDetailsViewModelFactory
    private lateinit var adapter: HomeDetailsAdapter

    override fun createViewBinding(): FragmentHomeDetailsBinding {
        return FragmentHomeDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val news: DetailsUi? = arguments?.parcelable("item")

        if (news != null) {
            viewModelFactory = HomeDetailsViewModelFactory(news)
            viewModel = ViewModelProvider(this, viewModelFactory)[HomeDetailsViewModel::class.java]
        }
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            adapter = HomeDetailsAdapter { position ->
                viewModel.handleShowImageClick(position)
            }

            imageCarousel.layoutManager = LinearLayoutManager(requireContext())
            imageCarousel.adapter = adapter

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
                    when (news.imagesUriList.size) {
                        0 -> {
                            imageCarousel.visibility = GONE
                            dotsCarousel.visibility = GONE
                        }

                        1 -> {
                            imageCarousel.visibility = VISIBLE
                            dotsCarousel.visibility = GONE
                        }

                        else -> {
                            imageCarousel.visibility = VISIBLE
                            dotsCarousel.visibility = VISIBLE
                        }
                    }
                    adapter.setItems(news.imagesUriList)
                } else {
                    imageCarousel.visibility = GONE
                    dotsCarousel.visibility = GONE
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