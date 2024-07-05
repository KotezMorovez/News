package com.example.news.ui.profile.main

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
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.FragmentProfileBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.common.AppBarStateChangeListener
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.RecyclerItemDecorator
import com.example.news.ui.profile.main.adapter.ProfileAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ProfileViewModel>
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }
    private val adapter: ProfileAdapter = ProfileAdapter()
    private lateinit var selectedImageUri: Uri

    override fun createViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        viewModel.loadProfile()
//        setTranslucentStatusBar(true)
        with(viewBinding) {
            val decoration = RecyclerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.profile_info_divider,
                    null
                )!!
            )
            infoRecyclerView.addItemDecoration(decoration)
            infoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            infoRecyclerView.adapter = adapter

            editImageButton.setOnClickListener {
                selectImage()
            }

            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = ""
            toolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }

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

            sourcesButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_sourcesFragment)
            }

            languagesButton.setOnClickListener {
                this@ProfileFragment.findNavController()
                    .navigate(R.id.action_profileFragment_to_languagesFragment)
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
            profileAppBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
                        toolbar.setBackgroundColor(resources.getColor(R.color.blue_700, null))
                    }
                    if (state == State.IDLE)
                        toolbar.setBackgroundColor(resources.getColor(R.color.blue_700_08, null))
                }
            })

            collapsingToolbar.setStatusBarScrimColor(resources.getColor(R.color.blue_700, null))

            profileImage.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    val corner = 48f
                    outline?.setRoundRect(0, -corner.toInt(), view!!.width, view.height, corner)
                }
            }
            profileImage.clipToOutline = true
        }
    }

    override fun onStart() {
        setTranslucentStatusBar(true)
        super.onStart()
    }
    override fun onStop() {
        setTranslucentStatusBar(false)
        super.onStop()
    }

    private fun setTranslucentStatusBar(isTranslucent: Boolean) {
        val window = requireActivity().window
        if (isTranslucent) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = resources.getColor(R.color.blue_700_08, null)
        } else {
            window.statusBarColor = resources.getColor(R.color.blue_700, null)
            WindowCompat.setDecorFitsSystemWindows(window, true)
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
                viewModel.uploadImage(selectedImageUri, requireContext().contentResolver)
            }
        }
    }

    companion object {
        const val READ_GALLERY_REQUEST_CODE = 111
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
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.profileInfoLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        viewModel.image.observe(viewLifecycleOwner) {
            with(viewBinding) {
                Glide.with(profileImage)
                    .load(it)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(profileImage)
            }
        }
    }
}