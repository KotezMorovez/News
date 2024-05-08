package com.example.news.ui.profile.sources

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentSourcesBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.RecyclerItemDecorator
import com.example.news.ui.profile.sources.adapter.SourcesAdapter
import com.google.android.material.snackbar.Snackbar

class SourcesFragment : BaseFragment<FragmentSourcesBinding>() {
    lateinit var viewModel: SourcesViewModel
    private val adapter: SourcesAdapter = SourcesAdapter()

    override fun createViewBinding(): FragmentSourcesBinding {
        return FragmentSourcesBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[SourcesViewModel::class.java]

        viewModel.loadData()

        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.sources_title)
            setHasOptionsMenu(true)
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            val decoration = RecyclerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.profile_info_divider, null
                )!!
            )
            sourcesRecyclerView.addItemDecoration(decoration)
            sourcesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            sourcesRecyclerView.adapter = adapter
            adapter.setOnCheckBoxClickListener { id, isUse ->
                viewModel.setSourceListChanges(id, isUse)
            }

            chooseAllCheckBox.isChecked = viewModel.isCheckBoxPressed
            chooseAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSelectAllStateChanged(isChecked)
            }
        }
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("inflater.inflate(R.menu.save_menu, menu)", "com.example.news.R")
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.saveSourcesList()
        return true
    }

    override fun observeData() {
        viewModel.successEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(R.string.sources_save_success),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()

            this@SourcesFragment.findNavController()
                .navigate(R.id.action_sourcesFragment_to_profileFragment)
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

        viewModel.sources.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}