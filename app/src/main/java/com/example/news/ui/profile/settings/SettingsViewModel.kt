package com.example.news.ui.profile.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.repository.NewsRepositoryImpl
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.model.profile.SourcesList
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.profile.settings.adapter.SettingsSourceItem
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()
    private val newsRepository: NewsRepository = NewsRepositoryImpl.getInstance()
    private var currentProfile: Profile? = null

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _successEvent = SingleLiveEvent<Unit>()
    val successEvent: LiveData<Unit>
        get() = _successEvent

    private val _sources = MutableLiveData<List<SettingsSourceItem>>(listOf())
    val sources: LiveData<List<SettingsSourceItem>>
        get() = _sources

    private var userSources: MutableList<String> = mutableListOf()

    var isCheckBoxPressed = false
        private set

    fun loadData() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                currentProfile = result.getOrNull()
                val loadAPISourcesJob = getAPISources()
                val loadUserSourcesJob = getUserSources()

                val sources = loadAPISourcesJob.await()
                val profileSources = loadUserSourcesJob.await()

                if (profileSources != null && sources != null) {
                    userSources = profileSources.sources.toMutableList()
                    _sources.value = sources.sources
                        .map {
                            if (userSources.contains(it.id)) {
                                SettingsSourceItem(
                                    id = it.id,
                                    text = it.name,
                                    isUse = true
                                )
                            } else {
                                SettingsSourceItem(
                                    id = it.id,
                                    text = it.name,
                                    isUse = false
                                )
                            }
                        }
                }
            }
        }
    }

    fun setSourceListChanges(id: String, isUse: Boolean) {
        if (isUse) {
            userSources.add(id)
            toggleItemInList(id)
        } else {
            userSources.remove(id)
            toggleItemInList(id)
        }
    }

    fun onSelectAllStateChanged(newState: Boolean) {
        isCheckBoxPressed = newState

        changeSelection(newState)
    }

    fun saveSourcesList() {
        viewModelScope.launch {
            if (currentProfile != null && userSources.isNotEmpty()) {
                val result =
                    profileRepository.updateProfileData(currentProfile!!.copy(sources = userSources))
                if (result.isSuccess) {
                    _successEvent.call()
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.value = R.string.settings_save_error
                    }
                }
            } else if (userSources.isEmpty()) {
                _errorEvent.value = R.string.settings_empty_resources_error
            } else {
                _errorEvent.value = R.string.settings_profile_error
            }
        }
    }

    private fun changeSelection(selection: Boolean) {
        userSources = if (selection) {
            _sources.value!!.map {
                it.id
            }.toMutableList()
        } else {
            mutableListOf()
        }

        val newSourcesList = _sources.value
        _sources.value = newSourcesList?.map {
            it.copy(isUse = selection)
        }
    }

    private fun toggleItemInList(id: String) {
        val oldSources = _sources.value ?: listOf()
        _sources.value = oldSources.map {
            if (it.id == id) {
                return@map it.copy(isUse = !it.isUse)
            }
            return@map it
        }
    }

    private fun getUserSources(): Deferred<Profile?> {
        return viewModelScope.async {
            val result = profileRepository.getProfile()

            return@async if (result.isSuccess) {
                result.getOrNull()
            } else {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    _errorEvent.value = R.string.settings_error
                }
                null
            }
        }
    }

    private fun getAPISources(): Deferred<SourcesList?> {
        return viewModelScope.async {
            val result = newsRepository.getSources()

            return@async if (result.isSuccess) {
                result.getOrNull()
            } else {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    _errorEvent.value = R.string.settings_error
                }
                null
            }
        }
    }
}