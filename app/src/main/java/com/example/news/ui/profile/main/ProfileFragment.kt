package com.example.news.ui.profile.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.profile.ProfileViewModel
import com.example.news.ui.profile.adapter.ProfileAdapter
import com.example.news.ui.profile.edit.ProfileEditViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var viewModel: ProfileViewModel
    private val adapter: ProfileAdapter = ProfileAdapter()

    override fun createViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun initUi(isFirstLaunch: Boolean) {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        with(viewBinding) {
            infoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            infoRecyclerView.adapter = adapter
            viewModel.loadProfile()

            profileImage.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("imageUrl", viewModel.image.value)

                this@ProfileFragment.findNavController()
                    .navigate(
                        R.id.action_profileFragment_to_profileShowImageFragment,
                        bundle
                    )
            }

            editButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_profileEditFragment)
            }

            deleteButton.setOnClickListener {
                val message = resources.getText(R.string.profile_dialog_delete).toString()
                ProfileDialogFragment(message) {
                    viewModel.deleteAccount()
                }.show(
                    childFragmentManager,
                    ProfileDialogFragment.TAG
                )
            }

            exitButton.setOnClickListener {
                val message = resources.getText(R.string.profile_dialog_exit).toString()
                ProfileDialogFragment(message) {
                    viewModel.logout()
                }.show(
                    childFragmentManager,
                    ProfileDialogFragment.TAG
                )
            }
        }
    }

    override fun observeData() {
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(it),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.goToAuthEvent.observe(viewLifecycleOwner) {
            this@ProfileFragment.findNavController()
                .navigate(R.id.action_profileFragment_to_loginFragment)
        }

        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        viewModel.image.observe(viewLifecycleOwner) {
            with(viewBinding) {
                Glide.with(profileImage)
                    .load(it)
                    .placeholder(R.drawable.avatar_placeholder)
                    .circleCrop()
                    .into(profileImage)
            }
        }
    }
}