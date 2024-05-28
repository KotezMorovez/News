package com.example.news.ui.profile.edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Outline
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileEditBinding
import com.example.news.di.AppComponentHolder
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.profile.main.ProfileDialogFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>() {
    @Inject
    lateinit var viewModelFactory: ProfileEditViewModelFactory
    private val viewModel: ProfileEditViewModel by viewModels { viewModelFactory }
    private lateinit var selectedImageUri: Uri

    companion object {
        const val READ_GALLERY_REQUEST_CODE = 111
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

            editImageButton.setOnClickListener {
                clearAllFocus()
                selectImage()
            }

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

    @Deprecated("Deprecated in Java")
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