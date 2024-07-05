package com.example.news.ui.profile.edit

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val userRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _profileEditLiveData: MutableLiveData<ProfileEditItem> = MutableLiveData()

    val profileEditLiveData: LiveData<ProfileEditItem>
        get() = _profileEditLiveData

    private val _goToProfileScreen: SingleLiveEvent<Unit> = SingleLiveEvent()

    val goToProfileScreen: LiveData<Unit>
        get() = _goToProfileScreen

    private val _errorEvent: SingleLiveEvent<Int> = SingleLiveEvent()

    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _goToAuthEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val goToAuthEvent: LiveData<Unit>
        get() = _goToAuthEvent

    private var currentUserId: String = ""
    private var currentUserEmail: String = ""
    private var currentUserName: String = ""
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
                        email = profile.email,
                        language = profile.language,
                        sources = profile.sources
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
            val validName = isValidName(_profileEditLiveData.value!!.name)

            if (
                validName &&
                profileEditLiveData.value!!.name != currentUserName
            ) {
                val profile = Profile(
                    name = _profileEditLiveData.value!!.name,
                    email = currentUserEmail,
                    imageUrl = _profileEditLiveData.value!!.imageURL,
                    id = currentUserId,
                    language = _profileEditLiveData.value!!.language,
                    sources = _profileEditLiveData.value!!.sources
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
                Log.e("News", "Error in image or name")
                _errorEvent.value = R.string.profile_edit_save_error
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val result = authRepository.deleteAccount()
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.profile_edit_delete_error
                }
            } else if (result.isSuccess) {
                _goToAuthEvent.call()
            }
        }
    }

    private fun isValidName(name: String): Boolean {
        val namePattern = "^[0-9a-zA-Z].{1,30}$"
        val pattern = Pattern.compile(namePattern)
        val matcher = pattern.matcher(name)

        return matcher.matches()
    }
}
