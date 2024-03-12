package com.example.news.ui.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.repository.NewsRepositoryImpl
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.common.IDelegateAdapterItem
import com.example.news.ui.homepage.adapter.item.NewsImageItem
import com.example.news.ui.homepage.adapter.item.NewsTextItem
import com.example.news.ui.homepage.adapter.item.NewsUi
import com.example.news.ui.homepage.adapter.item.NewsCarouselItem
import com.example.news.ui.homepage.adapter.item.NewsShowImageCarouselItem
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {
    private val newsRepository: NewsRepository = NewsRepositoryImpl.getInstance()
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()
    private val isTest = true

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _news = MutableLiveData<List<IDelegateAdapterItem>>(listOf())
    val news: LiveData<List<IDelegateAdapterItem>>
        get() = _news

    private val _errorEvent = SingleLiveEvent<Int>()

    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselItem>()

    val goToShowImageEvent: LiveData<NewsShowImageCarouselItem>
        get() = _goToShowImageEvent

    fun getProfileAvatar() {
        viewModelScope.launch {
            val result = profileRepository.getProfile()

            if (result.isSuccess) {
                val profile = result.getOrNull()
                if (profile != null) {

                    _image.value = profile.imageUrl ?: ""
                }

            } else if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("News", exception.stackTraceToString())
                    _errorEvent.value = R.string.home_page_show_avatar_toast_error
                }
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            if (isTest) {

                val list = listOf(
                    NewsImageItem(
                        NewsUi(
                            "1",
                            "TEST1",
                            "Test1",
                            listOf("https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png"),
                            "now",
                            false
                        )
                    ),
                    NewsTextItem(
                        NewsUi(
                            "2",
                            "TEST2",
                            "Test2",
                            listOf(),
                            "5 minutes ago",
                            true
                        )
                    ),
                    NewsCarouselItem(
                        NewsUi(
                            "3",
                            "TEST3",
                            "Test3",
                            listOf(
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png",
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png",
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png"
                            ),
                            "10 minutes ago",
                            true
                        )
                    ),
                    NewsCarouselItem(
                        NewsUi(
                            "4",
                            "TEST4",
                            "Test4",
                            listOf(
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png",
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png",
                                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Armenian_news_-_NEWS.am.png"
                            ),
                            "10 minutes ago",
                            true
                        )
                    )
                )

                _news.value = list
            }
        }
    }

    fun handleFavouriteItemClick(id: String) {
        val items = _news.value!!.toMutableList()

        val targetItemIndex = items.indexOfFirst { it.id() == id }

        if (targetItemIndex == -1) {
            return
        }

        val targetItem = items[targetItemIndex]
        val newItem: IDelegateAdapterItem

        when (targetItem) {
            is NewsTextItem -> {
                newItem = NewsTextItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
            }

            is NewsImageItem -> {
                newItem = NewsImageItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
            }

            is NewsCarouselItem -> {
                newItem =
                    NewsCarouselItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
            }

            else -> return
        }

        items.removeAt(targetItemIndex)
        items.add(targetItemIndex, newItem)

        _news.value = items
    }

    fun handleShowImageClick(id: String, position: Int) {
        val items = _news.value!!.toMutableList()
        val targetItemIndex = items.indexOfFirst { it.id() == id }

        if (targetItemIndex == -1) {
            return
        }

        val targetItem = items[targetItemIndex]

        when (targetItem) {
            is NewsImageItem -> {
                _goToShowImageEvent.value = NewsShowImageCarouselItem(targetItem.ui.imagesUriList, position)
            }

            is NewsCarouselItem -> {
                _goToShowImageEvent.value = NewsShowImageCarouselItem(targetItem.ui.imagesUriList, position)
            }

            else -> return
        }
    }
}