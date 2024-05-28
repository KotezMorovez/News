package com.example.news.ui.profile.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.data.repository.ProfileRepositoryImpl
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
    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _profileLiveData: MutableLiveData<List<ProfileInfoItem>> =
        MutableLiveData(listOf())
    val profileLiveData: LiveData<List<ProfileInfoItem>>
        get() = _profileLiveData

    private val _goToAuthEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val goToAuthEvent: LiveData<Unit>
        get() = _goToAuthEvent

    fun loadProfile() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                val profile = result.getOrNull()
                if (profile != null) {
                    val list = listOf(
                        ProfileInfoItem(text = profile.name, isBold = true),
                        ProfileInfoItem(text = profile.email, isBold = false)
                    )

                    _profileLiveData.value = list
                    _image.value = profile.imageUrl ?: ""
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