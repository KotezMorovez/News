package com.example.news.ui.home.favourite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.home.favourite.adapter.FavouriteItem
import com.example.news.ui.home.models.NewsShowImageCarouselUi
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselUi>()

    val goToShowImageEvent: LiveData<NewsShowImageCarouselUi>
        get() = _goToShowImageEvent

    private val _favourites = MutableLiveData<List<FavouriteItem>>(listOf())
    val favourites: LiveData<List<FavouriteItem>>
        get() = _favourites

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    fun handleShowImageClick(imageUrl: String) {
        _goToShowImageEvent.value = NewsShowImageCarouselUi(listOf(imageUrl), 0)
    }

    fun loadData() {
        viewModelScope.launch {
            val result = profileRepository.getFavourites()

            if (result.isSuccess) {
                _favourites.value = result.getOrNull()?.map {
                    FavouriteItem(
                        id = it.id,
                        title = it.title,
                        url = it.url,
                        image = it.image
                    )
                } ?: listOf()
            } else if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.home_page_error
                }
            }
        }
    }
}
