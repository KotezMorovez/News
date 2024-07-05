package com.example.news.ui.profile.main

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.common.ui.BitmapUtils
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.profile.main.adapter.ProfileInfoItem
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private var currentProfile: Profile? = null
    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _profileInfoLiveData: MutableLiveData<List<ProfileInfoItem>> =
        MutableLiveData(listOf())
    val profileInfoLiveData: LiveData<List<ProfileInfoItem>>
        get() = _profileInfoLiveData

    private val _goToAuthEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val goToAuthEvent: LiveData<Unit>
        get() = _goToAuthEvent

    fun loadProfile() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                currentProfile = result.getOrNull()
                if (currentProfile != null) {
                    val list = listOf(
                        ProfileInfoItem(text = currentProfile!!.name, isBold = true),
                        ProfileInfoItem(text = currentProfile!!.email, isBold = false)
                    )

                    _profileInfoLiveData.value = list
                    _image.value = currentProfile!!.imageUrl ?: ""
                    Log.e("NewsDebug", currentProfile!!.imageUrl.toString())
                }

            } else if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.profile_load_toast_error
                }
            }
        }
    }

    fun uploadImage(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            val bitmap = BitmapUtils.getBitmapFromUri(uri, contentResolver)
            val storageUriResult = profileRepository.saveImage(bitmap, currentProfile!!.id)

            if (storageUriResult.isFailure) {
                val exception = storageUriResult.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.profile_edit_upload_error
                }
            } else {
                saveImage(storageUriResult.getOrNull() ?: "")
            }
        }
    }

    private suspend fun saveImage(imageURL: String) {
        _image.value = imageURL
        val profile = Profile(
            name = currentProfile!!.name,
            email = currentProfile!!.email,
            imageUrl = imageURL,
            id = currentProfile!!.id,
            language = currentProfile!!.language,
            sources = currentProfile!!.sources
        )
        val result = profileRepository.updateProfileData(profile)
        if (result.isFailure) {
            val exception = result.exceptionOrNull()
            if (exception != null) {
                _errorEvent.value = R.string.profile_change_image_error
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = authRepository.logoutUser()
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.profile_exit_toast_error
                }
            } else if (result.isSuccess) {
                _goToAuthEvent.call()
            }
        }
    }
}