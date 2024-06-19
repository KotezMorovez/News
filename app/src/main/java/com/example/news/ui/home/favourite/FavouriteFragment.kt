package com.example.news.ui.home.favourite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentFavouriteBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.home.favourite.adapter.FavouriteAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FavouriteViewModel>

    private val viewModel: FavouriteViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]
    }

    private val adapter: FavouriteAdapter = FavouriteAdapter(
        onImageClickListener = { id ->
            viewModel.handleShowImageClick(id)
        },
        onLinkClickListener = { link ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent)
        }
    )

    override fun createViewBinding(): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        viewModel.loadData()

        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            newsRecyclerView.adapter = adapter
            newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun observeData() {
        viewModel.favourites.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.goToShowImageEvent.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putStringArray("imageUrlList", it.list.toTypedArray())
            bundle.putInt("position", it.position)

            this@FavouriteFragment.findNavController()
                .navigate(R.id.action_favouriteFragment_to_homeShowImageFragment, bundle)
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
    }
}