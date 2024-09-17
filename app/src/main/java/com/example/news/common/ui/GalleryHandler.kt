package com.example.news.common.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class GalleryHandler(
    fragment: Fragment,
    private val lambda: ((uri: Uri) -> Unit)
) {
    private val galleryResultLauncher: ActivityResultLauncher<Intent>
    private val pickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private val permissionLauncher: ActivityResultLauncher<String>
    private var isStoragePermissionGranted: Boolean = false
    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE

    init {
        galleryResultLauncher = initGalleryLauncher(fragment)
        pickerLauncher = initPickerLauncher(fragment)
        permissionLauncher = initPermissionLauncher(fragment)
        isStoragePermissionGranted = isStoragePermissionGranted(fragment)
    }

    fun selectImage() {
        if (Build.VERSION.SDK_INT < 33) {
            if (isStoragePermissionGranted) {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                galleryResultLauncher.launch(intent)
            } else {
                permissionLauncher.launch(permission)
            }
        } else {
            pickerLauncher.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ImageOnly)
                    .build()
            )
        }
    }

    private fun initGalleryLauncher(fragment: Fragment): ActivityResultLauncher<Intent> {
        return fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                if (data != null) {
                    lambda(data)
                }
            }
        }
    }

    private fun initPickerLauncher(fragment: Fragment):
            ActivityResultLauncher<PickVisualMediaRequest> {
        return fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                lambda(it)
            }
        }
    }

    private fun initPermissionLauncher(
        fragment: Fragment,
    ): ActivityResultLauncher<String> {
        return fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                selectImage()
            }
        }
    }

    private fun isStoragePermissionGranted(fragment: Fragment): Boolean {
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
