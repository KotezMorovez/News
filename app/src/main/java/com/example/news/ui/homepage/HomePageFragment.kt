package com.example.news.ui.homepage

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentHomePageBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.CompositeDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsCarouselDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsImageDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsTextDelegateAdapter
import com.google.android.material.snackbar.Snackbar

class HomePageFragment : BaseFragment<FragmentHomePageBinding>() {
    private lateinit var viewModel: HomePageViewModel
    private val compositeDelegateAdapter by lazy {
        CompositeDelegateAdapter.Builder()
            .add(NewsCarouselDelegateAdapter(
                onFavouriteClickListener = { itemId ->
                    viewModel.handleFavouriteItemClick(itemId)
                },
                onImageClickListener = { itemId, position ->
                    viewModel.handleShowImageClick(itemId, position)
                }
            ))
            .add(NewsImageDelegateAdapter(
                onFavouriteClickListener = { itemId ->
                    viewModel.handleFavouriteItemClick(itemId)
                },
                onImageClickListener = { itemId, position ->
                    viewModel.handleShowImageClick(itemId, position)
                }
            ))
            .add(NewsTextDelegateAdapter(onFavouriteClickListener = { itemId ->
                viewModel.handleFavouriteItemClick(itemId)
            }))
            .build()
    }

    override fun createViewBinding(): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        with(viewBinding) {
            newsRecyclerView.adapter = compositeDelegateAdapter
            newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            customToolbar.setGoToProfileListener {
                this@HomePageFragment.findNavController()
                    .navigate(R.id.action_homePageFragment_to_profileFragment)
            }

            customToolbar.setGoToFavouriteListener {
                this@HomePageFragment.findNavController()
//                    .navigate(R.id.action_homePageFragment_to_favoriteFragment)
            }

            viewModel.getProfileAvatar()
            viewModel.loadNews()
        }
    }

    override fun observeData() {
        viewModel.news.observe(viewLifecycleOwner) {
            compositeDelegateAdapter.submitList(it)
        }

        viewModel.image.observe(viewLifecycleOwner) {
            viewBinding.customToolbar.setImage(it)
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(it),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.goToShowImageEvent.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putStringArray("imageUrlList", it.list.toTypedArray())
            bundle.putInt("position", it.position)

            this@HomePageFragment.findNavController()
                .navigate(R.id.action_homePageFragment_to_homePageShowImageFragment, bundle)
        }
    }
}