package com.example.news.ui.home.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.home.models.NewsShowImageCarouselItem
import com.example.news.ui.home.models.DetailsUi

class HomeDetailsViewModel(
    private val news: DetailsUi
) : ViewModel() {
    private val _newsDetails = MutableLiveData(news)
    val newsDetails: LiveData<DetailsUi>
        get() = _newsDetails

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselItem>()
    val goToShowImageEvent: LiveData<NewsShowImageCarouselItem>
        get() = _goToShowImageEvent

    fun handleShowImageClick(position: Int) {
        val list = _newsDetails.value?.imagesUriList
        if (list != null) {
            _goToShowImageEvent.value = NewsShowImageCarouselItem(list, position)
        }
    }

    fun handleFavouriteClick() {
        news.isFavorite = !news.isFavorite
        _newsDetails.value = news
    }
}