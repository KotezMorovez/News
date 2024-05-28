package com.example.news.ui.home.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.home.response.Favourite
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.home.models.NewsShowImageCarouselUi
import com.example.news.ui.home.models.DetailsUi
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeDetailsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private lateinit var details: DetailsUi
    private val _newsDetails = MutableLiveData<DetailsUi>(null)
    val newsDetails: LiveData<DetailsUi>
        get() = _newsDetails

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselUi>()
    val goToShowImageEvent: LiveData<NewsShowImageCarouselUi>
        get() = _goToShowImageEvent

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent


    fun setDetails(details: DetailsUi?) {
        if (details != null) {
            this.details = details
            _newsDetails.value = details!!
        }
    }

    fun handleShowImageClick(position: Int) {
        val list = _newsDetails.value?.imagesUriList
        if (list != null) {
            _goToShowImageEvent.value = NewsShowImageCarouselUi(list, position)
        }
    }

    fun handleFavouriteClick() {
        val favouriteItem = Favourite(
            id = details.id,
            title = details.header,
            url = details.url,
            image = details.imagesUriList?.get(0) ?: "",
        )

        viewModelScope.launch {
            details = details.copy(isFavorite = !details.isFavorite)
            _newsDetails.value = details

            if (details.isFavorite) {
                profileRepository.addFavourite(favouriteItem, details.userId)
            } else {
                profileRepository.removeFavourite(favouriteItem, details.userId)
            }
        }
    }
}