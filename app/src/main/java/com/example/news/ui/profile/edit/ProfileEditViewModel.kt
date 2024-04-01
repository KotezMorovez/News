package com.example.news.ui.profile.edit

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.news.domain.model.profile.Profile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.domain.repository.ProfileRepository
import com.example.news.common.BitmapUtils
import com.example.news.ui.common.SingleLiveEvent
import kotlinx.coroutines.launch
import java.io.File
import java.util.regex.Pattern

class ProfileEditViewModel : ViewModel() {
    private val userRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()

    private val _profileEditLiveData: MutableLiveData<ProfileEditItem> = MutableLiveData()

    val profileEditLiveData: LiveData<ProfileEditItem>
        get() = _profileEditLiveData

    private val _goToProfileScreen: SingleLiveEvent<Unit> = SingleLiveEvent()

    val goToProfileScreen: LiveData<Unit>
        get() = _goToProfileScreen

    private val _errorEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val errorEvent: LiveData<Unit>
        get() = _errorEvent

    private lateinit var currentUserId: String
    private lateinit var currentUserEmail: String
    private lateinit var currentUserName: String
    private var currentUserImageUri: String? = null

    fun getUserInfo() {
        viewModelScope.launch {
            val result = userRepository.getProfile()

            if (result.isSuccess) {
                val profile = result.getOrNull()
                if (profile != null) {
                    currentUserId = profile.id
                    currentUserEmail = profile.email
                    currentUserName = profile.name
                    currentUserImageUri = profile.imageUrl

                    _profileEditLiveData.value = ProfileEditItem(
                        name = profile.name,
                        imageURL = profile.imageUrl ?: "",
                        email = profile.email
                    )
                }
            }
        }
    }

    fun setName(newName: String) {
        val oldProfileInfo = _profileEditLiveData.value ?: ProfileEditItem.default()
        _profileEditLiveData.value = oldProfileInfo.copy(name = newName)
    }

    fun saveData(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val selectedImageUri = _profileEditLiveData.value?.imageURL
            var image: String? = null
            val validName = isValidName(_profileEditLiveData.value!!.name)

            if (!selectedImageUri.isNullOrEmpty() && selectedImageUri != currentUserImageUri) {
                Log.i("News", selectedImageUri.toString())
                image = uploadImage(File(selectedImageUri).toUri(), contentResolver)
            }

            if (
                image != null &&
                validName &&
                profileEditLiveData.value!!.name != currentUserName
            ) {
                val profile = Profile(
                    name = profileEditLiveData.value!!.name,
                    email = currentUserEmail,
                    imageUrl = image,
                    id = currentUserId
                )

                val result = userRepository.updateProfileData(profile)
                if (result.isSuccess) {
                    _goToProfileScreen.call()
                } else if (result.isFailure) {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        _errorEvent.call()
                    }
                }
            } else {
                Log.e("News", "Null in image")
                _errorEvent.call()
            }
        }
    }

    private suspend fun uploadImage(uri: Uri, contentResolver: ContentResolver): String? {
        val bitmap = BitmapUtils.getBitmapFromUri(uri, contentResolver)

        val storageUriResult = userRepository.saveImage(bitmap, currentUserId)

        if (storageUriResult.isFailure) {
            val exception = storageUriResult.exceptionOrNull()
            if (exception != null) {
                Log.e("News", exception.stackTraceToString())
                _errorEvent.call()
            }
            return null
        }

        val storageUri = storageUriResult.getOrNull()

        if (storageUri != null) {
            saveImage(storageUri)
        }
        return storageUri
    }

    fun saveImage(imageURL: String) {
        val oldProfileInfo = _profileEditLiveData.value ?: ProfileEditItem.default()
        _profileEditLiveData.value = oldProfileInfo.copy(imageURL = imageURL)
    }

    private fun isValidName(name: String): Boolean {
        val namePattern = "^[0-9a-zA-Z].{1,30}$"
        val pattern = Pattern.compile(namePattern)
        val matcher = pattern.matcher(name)

        return matcher.matches()
    }
}
