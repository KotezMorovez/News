package com.example.news.ui.profile.languages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.common.Languages
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.profile.languages.adapter.LanguagesItem
import kotlinx.coroutines.launch

class LanguagesViewModel : ViewModel() {
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()
    private var currentProfile: Profile? = null
    private var userLanguage: String = ""
    private var oldId: String = ""

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _successEvent = SingleLiveEvent<Unit>()
    val successEvent: LiveData<Unit>
        get() = _successEvent

    private val _languages = MutableLiveData<List<LanguagesItem>>(listOf())
    val languages: LiveData<List<LanguagesItem>>
        get() = _languages

    fun loadData() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                currentProfile = result.getOrNull()

                if (currentProfile != null) {
                    userLanguage = currentProfile?.language ?: ""

                    _languages.value = Languages.entries.map {
                        if (userLanguage == (it.toString().lowercase())) {
                            oldId = it.toString().lowercase()
                            LanguagesItem(
                                id = oldId,
                                isCheck = true
                            )
                        } else {
                            LanguagesItem(
                                id = it.toString().lowercase(),
                                isCheck = false
                            )
                        }
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.value = R.string.languages_profile_error
                    }
                }
            } else {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.languages_save_error
                }
            }
        }
    }

    fun saveLanguage() {
        viewModelScope.launch {
            if (currentProfile != null && userLanguage.isNotEmpty()) {
                val result =
                    profileRepository.updateProfileData(currentProfile!!.copy(language = userLanguage))
                if (result.isSuccess) {
                    _successEvent.call()
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.value = R.string.languages_save_error
                    }
                }
            } else if (userLanguage.isEmpty()) {
                _errorEvent.value = R.string.languages_empty_resources_error
            } else {
                _errorEvent.value = R.string.languages_profile_error
            }
        }
    }

    fun setLanguagesListChanges(id: String, isCheck: Boolean) {
        if (isCheck) {
            userLanguage = id
            toggleItemInList(id)
        } else {
            userLanguage = ""
            toggleItemInList(id)
        }
    }

    private fun toggleItemInList(id: String) {
        val oldLang = _languages.value ?: listOf()

        _languages.value = oldLang.map {
            if (it.id == oldId) {
                return@map it.copy(isCheck = false)
            } else {
                return@map it
            }
        }.map {
            if (it.id == id) {
                oldId = id
                return@map it.copy(isCheck = true)
            } else {
                return@map it
            }
        }
    }
}