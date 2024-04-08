package com.example.news.ui.homepage

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.FragmentHomePageBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.CompositeDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsCarouselDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsEndingDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsImageDelegateAdapter
import com.example.news.ui.homepage.adapter.delegate_adapter.NewsTextDelegateAdapter
import com.example.news.ui.profile.ProfileActivity
import com.google.android.material.snackbar.Snackbar


class HomePageFragment : BaseFragment<FragmentHomePageBinding>() {
    private lateinit var viewModel: HomePageViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

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
            .add(NewsEndingDelegateAdapter())
            .build()
    }

    override fun createViewBinding(): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFactory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomePageViewModel::class.java]
        super.onCreate(savedInstanceState)

        viewModel.getProfileAvatar()

        if (viewModel.isFirstLaunch) {
            viewModel.loadNews()
            viewModel.isFirstLaunch = false
        }
    }

    override fun initUi() {
        with(viewBinding) {
            newsSwipeRefresh.setColorSchemeResources(
                R.color.blue_500,
                R.color.blue_600,
                R.color.blue_700,
                R.color.blue_800
            )
            newsSwipeRefresh.setOnRefreshListener {
                viewModel.resetPagination()
                viewModel.loadNews()
            }

            newsRecyclerView.adapter = compositeDelegateAdapter
            newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            newsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        if (viewModel.isPaginationEnabled && isLastVisible()) {
                            viewModel.loadNews()
                        }
                    }
                }
            })

            customToolbar.onProfileIconClickedListener {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(intent)
            }

            customToolbar.setOnFavouriteIconClickedListener {
                this@HomePageFragment.findNavController()
//                    .navigate(R.id.action_homePageFragment_to_favoriteFragment)
            }

            customToolbar.setOnSearchTextChangeListener {
                viewModel.applySearchText(it)
            }
            customToolbar.setOnSearchCanceledListener {
                viewModel.resetSearchField()
            }
        }
    }

    private fun isLastVisible(): Boolean{
        with(viewBinding){
            val layoutManager = newsRecyclerView.layoutManager as LinearLayoutManager
            val pos = layoutManager.findLastCompletelyVisibleItemPosition()
            val numItems = (newsRecyclerView.adapter as CompositeDelegateAdapter).itemCount
            return (pos >= numItems - 1)
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

        viewModel.endRefreshingEvent.observe(viewLifecycleOwner){
            viewBinding.newsSwipeRefresh.isRefreshing = false
        }
    }
}