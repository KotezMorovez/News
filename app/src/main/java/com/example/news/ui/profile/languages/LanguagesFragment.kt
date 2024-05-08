package com.example.news.ui.profile.languages

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentLanguagesBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.RecyclerItemDecorator
import com.example.news.ui.profile.languages.adapter.LanguagesAdapter
import com.google.android.material.snackbar.Snackbar

class LanguagesFragment: BaseFragment<FragmentLanguagesBinding>() {
    private lateinit var viewModel: LanguagesViewModel
    private val adapter: LanguagesAdapter = LanguagesAdapter()

    override fun createViewBinding(): FragmentLanguagesBinding {
        return FragmentLanguagesBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this) [LanguagesViewModel::class.java]

        viewModel.loadData()

        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.languages_title)
            setHasOptionsMenu(true)
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

            val decoration = RecyclerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.profile_info_divider, null
                )!!
            )
            languagesRecyclerView.addItemDecoration(decoration)
            languagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            languagesRecyclerView.adapter = adapter
            adapter.setOnCheckBoxClickListener { id, isChecked ->
                viewModel.setLanguagesListChanges(id, isChecked)
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
        viewModel.saveLanguage()
        return true
    }

    override fun observeData() {
        viewModel.successEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(R.string.languages_save_success),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()

            this@LanguagesFragment.findNavController()
                .navigate(R.id.action_languagesFragment_to_profileFragment)
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

        viewModel.languages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}