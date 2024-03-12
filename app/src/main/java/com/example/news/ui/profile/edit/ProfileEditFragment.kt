package com.example.news.ui.profile.edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileEditBinding
import com.example.news.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar

class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>() {
    private lateinit var viewModel: ProfileEditViewModel
    private lateinit var selectedImageUri: Uri

    companion object {
        const val READ_GALLERY_REQUEST_CODE = 111
    }

    override fun createViewBinding(): FragmentProfileEditBinding {
        return FragmentProfileEditBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[ProfileEditViewModel::class.java]

        with(viewBinding) {
            nameProfileEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.setName(nameProfileEditText.text.toString())
                }
            }

            editImageButton.setOnClickListener {
                clearAllFocus()
                selectImage()
            }

            saveButton.setOnClickListener {
                clearAllFocus()

                if (viewModel.isValidName(nameProfileEditText.text.toString())) {
                    viewModel.saveData(requireContext().contentResolver)

                    nameProfileContainer.error = null
                } else {
                    nameProfileContainer.error = resources.getText(R.string.profile_edit_name_error)
                }
            }

            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.title = null
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun observeData() {
        viewModel.profileEditLiveData.observe(viewLifecycleOwner) {
            with(viewBinding) {
                nameProfileEditText.setText(it.name)
                emailProfileEditText.setText(it.email)

                Glide.with(profileImage)
                    .load(it.imageURL)
                    .circleCrop()
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(profileImage)
            }
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

    fun clearAllFocus() {
        with(viewBinding) {
            nameProfileEditText.clearFocus()
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted) {
            requestPermissions(arrayOf(permission), READ_GALLERY_REQUEST_CODE)
        }

        return isPermissionGranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_GALLERY_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        }
    }

    private fun selectImage() {
        if (isStoragePermissionGranted()) {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            galleryResultLauncher.launch(intent)
        }
    }

    private var galleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data = result.data
            if (data != null && data.data != null) {
                selectedImageUri = data.data!!
                viewModel.saveImage(selectedImageUri.toString())
            }
        }
    }
}