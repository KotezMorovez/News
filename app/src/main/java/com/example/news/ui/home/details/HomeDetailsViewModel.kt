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
import com.example.news.ui.home.models.NewsShowImageCarouselUi
import com.example.news.ui.home.models.DetailsUi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeDetailsViewModel(
    private var news: DetailsUi
) : ViewModel() {
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()

    private val _newsDetails = MutableLiveData(news)
    val newsDetails: LiveData<DetailsUi>
        get() = _newsDetails

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselUi>()
    val goToShowImageEvent: LiveData<NewsShowImageCarouselUi>
        get() = _goToShowImageEvent

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    fun handleShowImageClick(position: Int) {
        val list = _newsDetails.value?.imagesUriList
        if (list != null) {
            _goToShowImageEvent.value = NewsShowImageCarouselUi(list, position)
        }
    }

    fun handleFavouriteClick() {
        val favouriteItem = Favourite(
            id = news.id,
            title = news.header,
            url = news.url,
            image = news.imagesUriList?.get(0) ?: "",
        )

        viewModelScope.launch {
            news = news.copy(isFavorite = !news.isFavorite)
            _newsDetails.value = news

            if (news.isFavorite) {
                profileRepository.addFavourite(favouriteItem, news.userId)
            } else {
                profileRepository.removeFavourite(favouriteItem, news.userId)
            }
        }
    }
}