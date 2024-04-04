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
import com.example.news.ui.homepage.adapter.item.NewsCarouselItem
import com.example.news.ui.homepage.adapter.item.NewsShowImageCarouselItem
import kotlinx.coroutines.launch
import com.example.news.domain.model.home_page.request.NewsEverythingRequest
import com.example.news.domain.model.home_page.response.Article
import com.example.news.ui.common.DateUtils
import com.example.news.ui.homepage.adapter.item.NewsEndingItem
import com.example.news.ui.homepage.adapter.item.NewsUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class HomePageViewModel(private val dateUtils: DateUtils) : ViewModel() {
    var isFirstLaunch = true
    private val newsRepository: NewsRepository = NewsRepositoryImpl.getInstance()
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl.getInstance()
    private var page = 1
    var isPaginationEnabled = true
    private var currentQuery: String? = null

    fun applySearchText(text: String) {
        if (text == currentQuery) {
            return
        }
        currentQuery = text
        resetPagination()
        loadNews()
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

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

    private val _endRefreshingEvent = SingleLiveEvent<Unit>()

    val endRefreshingEvent: LiveData<Unit>
        get() = _endRefreshingEvent

    private var loadNewsJob: Job? = null

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
        if (isPaginationEnabled) {
            val queryText = if (currentQuery.isNullOrBlank()) null else currentQuery
            isPaginationEnabled = false

            loadNewsJob?.cancel()
            loadNewsJob = viewModelScope.launch {
                val request = if (queryText == null) {
                    NewsEverythingRequest(
                        page = page,
                        pageSize = PAGE_SIZE,
                        sources = listOf(
                            "google-news",
                            "abc-news",
                            "ars-technica",
                            "associated-press",
                            "bbc-news",
                            "wired"
                        ),
                        language = "en"
                    )
                } else {
                    NewsEverythingRequest(
                        query = queryText,
                        page = page,
                        pageSize = PAGE_SIZE,
                        language = "en"
                    )
                }

                val result = newsRepository.getNews(request)

                if (result.isFailure) {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        if(exception !is CancellationException) {
                            _errorEvent.value = R.string.home_page_search_toast_error
                        }
                    }
                    isPaginationEnabled = true
                    return@launch
                }

                val newsArticles = result.getOrNull()
                if (newsArticles != null) {
                    val oldResults = mutableListOf<IDelegateAdapterItem>()

                    if (_news.value != null) {
                        oldResults.addAll(_news.value!!)
                    }
                    val newResult = newsArticles.articles.map {
                        val elapsed = dateUtils.getElapsedTime(it.publishedAt)

                        if (it.imageUrl != null) {
                            NewsImageItem(
                                NewsUi(
                                    id = it.id,
                                    header = it.title,
                                    body = it.description ?: "",
                                    imagesUriList = listOf(it.imageUrl),
                                    date = elapsed,
                                    isFavorite = false,
                                )
                            )
                        } else {
                            NewsTextItem(
                                NewsUi(
                                    id = it.id,
                                    header = it.title,
                                    body = it.description ?: "",
                                    imagesUriList = null,
                                    date = elapsed,
                                    isFavorite = false
                                )
                            )
                        }
                    }
                    oldResults.addAll(newResult)

                    val fullPagesCount = newsArticles.totalResults / PAGE_SIZE
                    val hasRemainder = newsArticles.totalResults % PAGE_SIZE > 0
                    val allPagesCount = fullPagesCount + (if (hasRemainder) 1 else 0)
                    if (allPagesCount > page) {
                        isPaginationEnabled = true
                        page++
                    } else {
                        isPaginationEnabled = false

                        oldResults.add(
                            NewsEndingItem(
                                results = newsArticles.totalResults
                            )
                        )

                        _news.value = oldResults
                        _endRefreshingEvent.call()
                        return@launch
                    }

                    _news.value = oldResults
                    _endRefreshingEvent.call()
                }
            }
        }
    }

    fun resetPagination() {
        isPaginationEnabled = true
        page = 1
        _news.value = emptyList()
    }

    fun resetSearchField() {
        currentQuery = ""
        applySearchText(currentQuery!!)
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

        when (val targetItem = items[targetItemIndex]) {
            is NewsImageItem -> {
                _goToShowImageEvent.value =
                    NewsShowImageCarouselItem(targetItem.ui.imagesUriList!!, position)
            }

            is NewsCarouselItem -> {
                _goToShowImageEvent.value =
                    NewsShowImageCarouselItem(targetItem.ui.imagesUriList!!, position)
            }

            else -> return
        }
    }
}