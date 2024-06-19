package com.example.news.ui.profile.sources

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
import com.example.news.ui.profile.sources.adapter.SourcesItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourcesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {
    private var currentProfile: Profile? = null

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _successEvent = SingleLiveEvent<Unit>()
    val successEvent: LiveData<Unit>
        get() = _successEvent

    private val _sources = MutableLiveData<List<SourcesItem>>(listOf())
    val sources: LiveData<List<SourcesItem>>
        get() = _sources

    private var userSources: MutableList<String> = mutableListOf()

    var isCheckBoxPressed = false
        private set

    fun loadData() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                currentProfile = result.getOrNull()
                val sources = getAPISources()

                if (sources != null) {
                    userSources = currentProfile?.sources
                        ?.filter { userSourceId -> sources.sources.firstOrNull { it.id == userSourceId } != null }
                        ?.toMutableList() ?: mutableListOf()

                    _sources.value = sources.sources
                        .map {
                            if (userSources.contains(it.id)) {
                                SourcesItem(
                                    id = it.id,
                                    text = it.name,
                                    isUse = true
                                )
                            } else {
                                SourcesItem(
                                    id = it.id,
                                    text = it.name,
                                    isUse = false
                                )
                            }
                        }
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.value = R.string.sources_profile_error
                    }
                }
            } else {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.sources_save_error
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
                        _errorEvent.value = R.string.sources_save_error
                    }
                }
            } else if (userSources.isEmpty()) {
                _errorEvent.value = R.string.sources_empty_resources_error
            } else {
                _errorEvent.value = R.string.sources_profile_error
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

    private suspend fun getAPISources(): SourcesList? {
        val result = newsRepository.getSources(currentProfile!!.language)

        return if (result.isSuccess) {
            result.getOrNull()
        } else {
            val exception = result.exceptionOrNull()
            if (exception != null) {
                _errorEvent.value = R.string.sources_error
            }
            null

        }
    }
}