package com.example.news.ui.profile.settings

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentSettingsBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.RecyclerItemDecorator
import com.example.news.ui.profile.settings.adapter.SettingsSourceAdapter
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    lateinit var viewModel: SettingsViewModel
    private val adapter: SettingsSourceAdapter = SettingsSourceAdapter()

    override fun createViewBinding(): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        viewModel.loadData()

        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.settings_sources_title)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.saveSourcesList()
        return true
    }

    override fun observeData() {
        viewModel.successEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(R.string.settings_save_success),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()

            this@SettingsFragment.findNavController()
                .navigate(R.id.action_settingsFragment_to_profileFragment)
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