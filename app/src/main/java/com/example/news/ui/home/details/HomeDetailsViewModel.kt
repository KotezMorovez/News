package com.example.news.ui.home.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.domain.model.home.response.Favourite
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.home.models.NewsShowImageCarouselItem
import com.example.news.ui.home.models.DetailsUi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeDetailsViewModel(
    private val news: DetailsUi
) : ViewModel() {
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()
    private var currentUser: Profile? = null
    private var isFavouriteFlag = false

    private val _newsDetails = MutableLiveData(news)
    val newsDetails: LiveData<DetailsUi>
        get() = _newsDetails

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselItem>()
    val goToShowImageEvent: LiveData<NewsShowImageCarouselItem>
        get() = _goToShowImageEvent

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    fun handleShowImageClick(position: Int) {
        val list = _newsDetails.value?.imagesUriList
        if (list != null) {
            _goToShowImageEvent.value = NewsShowImageCarouselItem(list, position)
        }
    }

    fun handleFavouriteClick() {
        news.isFavorite = !news.isFavorite
        _newsDetails.value = news

        val favouriteItem = Favourite(
            id = news.id,
            title = news.header,
            url = news.url,
            image = news.imagesUriList?.get(0) ?: "",
        )

        viewModelScope.launch {
            getCurrentUser()
        }

        if (isFavouriteFlag) {
            isFavouriteFlag = false
            viewModelScope.launch {
                if (currentUser != null) {
                    profileRepository.addFavourite(favouriteItem, currentUser!!.id)
                }
            }
        } else {
            if (currentUser != null) {
                viewModelScope.launch {
                    profileRepository.removeFavourite(favouriteItem, currentUser!!.id)
                }
            }
        }
    }

    private suspend fun getCurrentUser() {
        val result = profileRepository.getProfile()
        if (result.isSuccess) {
            currentUser = result.getOrNull()
        } else {
            val exception = result.exceptionOrNull()
            if (exception != null) {
                Log.e("News", exception.stackTraceToString())
                _errorEvent.value = R.string.home_page_error
            }
        }
    }
}