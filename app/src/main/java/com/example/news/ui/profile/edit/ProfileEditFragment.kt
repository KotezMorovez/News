package com.example.news.ui.profile.edit

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileEditBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.profile.main.ProfileDialogFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ProfileEditViewModel>
    private val viewModel: ProfileEditViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileEditViewModel::class.java]
    }

    override fun createViewBinding(): FragmentProfileEditBinding {
        return FragmentProfileEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        with(viewBinding) {
            profileImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    val corner = 48f
                    outline?.setRoundRect(0, -corner.toInt(), view!!.width, view.height, corner)
                }
            }
            profileImage.clipToOutline = true

            nameProfileEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.setName(nameProfileEditText.text.toString())
                }
            }

            saveButton.setOnClickListener {
                clearAllFocus()
                viewModel.saveData(requireContext().contentResolver)
            }

            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
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
        }
    }

    override fun observeData() {
        viewModel.profileEditLiveData.observe(viewLifecycleOwner) {
            with(viewBinding) {
                nameProfileEditText.setText(it.name)
                emailProfileEditText.text = it.email

                Glide.with(profileImage)
                    .load(it.imageURL)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(profileImage)
            }
        }

        viewModel.goToAuthEvent.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.errorEvent.observe(viewLifecycleOwner){
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getText(R.string.profile_edit_save_error),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.goToProfileScreen.observe(viewLifecycleOwner){
            this@ProfileEditFragment.findNavController()
                .navigate(R.id.action_profileEditFragment_to_profileFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserInfo()
    }

    override fun onDestroy() {
        clearAllFocus()
        super.onDestroy()
    }

    private fun clearAllFocus() {
        with(viewBinding) {
            nameProfileEditText.clearFocus()
        }
    }
}