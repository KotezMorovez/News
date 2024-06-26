package com.example.news.ui.home.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.response.Article
import com.example.news.domain.model.home.response.Favourite
import com.example.news.domain.model.profile.Profile
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.DateUtils
import com.example.news.ui.common.SingleLiveEvent
import com.example.news.ui.common.delegate_adapter.IDelegateAdapterItem
import com.example.news.ui.home.main.adapter.item.NewsCarouselItem
import com.example.news.ui.home.main.adapter.item.NewsEndingItem
import com.example.news.ui.home.main.adapter.item.NewsImageItem
import com.example.news.ui.home.main.adapter.item.NewsTextItem
import com.example.news.ui.home.main.adapter.item.NewsUi
import com.example.news.ui.home.models.DetailsUi
import com.example.news.ui.home.models.NewsShowImageCarouselUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    var isFirstLaunch = true
    var isPaginationEnabled = true
    private var favouriteSet: MutableSet<String> = mutableSetOf()
    private var page = 1
    private var currentQuery: String? = null
    private var currentUser: Profile? = null
    private var allArticles: MutableList<Article> = mutableListOf()
    private var loadNewsJob: Job? = null

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _news = MutableLiveData<List<IDelegateAdapterItem>>(listOf())
    val news: LiveData<List<IDelegateAdapterItem>>
        get() = _news

    private val _errorEvent = SingleLiveEvent<Int>()
    val errorEvent: LiveData<Int>
        get() = _errorEvent

    private val _goToShowImageEvent = SingleLiveEvent<NewsShowImageCarouselUi>()
    val goToShowImageEvent: LiveData<NewsShowImageCarouselUi>
        get() = _goToShowImageEvent

    private val _dataInitEvent = SingleLiveEvent<Unit>()
    val dataInitEvent: LiveData<Unit>
        get() = _dataInitEvent

    private val _endRefreshingEvent = SingleLiveEvent<Unit>()
    val endRefreshingEvent: LiveData<Unit>
        get() = _endRefreshingEvent

    private val _goToDetailsEvent = SingleLiveEvent<DetailsUi>()
    val goToDetailsEvent: LiveData<DetailsUi>
        get() = _goToDetailsEvent

    fun applySearchText(text: String) {
        if (text == currentQuery) {
            return
        }
        currentQuery = text
        resetPagination()
        loadNews()
    }

    fun initialize() {
        viewModelScope.launch {
            getCurrentUser()
            _dataInitEvent.call()
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            getCurrentUser()
        }
    }

    fun loadNews() {
        if (isPaginationEnabled) {
            val queryText = if (currentQuery.isNullOrBlank()) null else currentQuery
            isPaginationEnabled = false

            loadNewsJob?.cancel()
            loadNewsJob = viewModelScope.launch {
                if (currentUser != null) {
                    val request = getRequest(queryText, currentUser!!)
                    val result = newsRepository.getNews(request)

                    if (result.isFailure) {
                        val exception = result.exceptionOrNull()
                        if (exception != null) {
                            Log.e("News", exception.stackTraceToString())
                            if (exception !is CancellationException) {
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

                        allArticles.addAll(newsArticles.articles)

                        val newResult = newsArticles.articles.map {
                            mapArticleToItem(it)
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
    }

    fun resetPagination() {
        isPaginationEnabled = true
        page = 1
        _news.value = emptyList()
        allArticles = mutableListOf()
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
        val isFavouriteFlag: Boolean

        when (targetItem) {
            is NewsTextItem -> {
                newItem =
                    NewsTextItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
                isFavouriteFlag = !targetItem.ui.isFavorite
            }

            is NewsImageItem -> {
                newItem =
                    NewsImageItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
                isFavouriteFlag = !targetItem.ui.isFavorite
            }

            is NewsCarouselItem -> {
                newItem =
                    NewsCarouselItem(targetItem.ui.copy(isFavorite = !targetItem.ui.isFavorite))
                isFavouriteFlag = !targetItem.ui.isFavorite
            }

            else -> return
        }

        val targetArticle = allArticles.firstOrNull { it.id == targetItem.id() }

        if (targetArticle != null) {
            val favouriteItem = Favourite(
                id = targetItem.id(),
                title = targetArticle.title ?: "",
                url = targetArticle.url,
                image = targetArticle.imageUrl ?: "",
            )

            if (isFavouriteFlag) {
                viewModelScope.launch {
                    favouriteSet.add(favouriteItem.id)
                    profileRepository.addFavourite(favouriteItem, currentUser!!.id)
                }
            } else {
                viewModelScope.launch {
                    favouriteSet.remove(favouriteItem.id)
                    profileRepository.removeFavourite(favouriteItem, currentUser!!.id)
                }
            }
        }

        items[targetItemIndex] = newItem
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
                    NewsShowImageCarouselUi(targetItem.ui.imagesUriList!!, position)
            }

            is NewsCarouselItem -> {
                _goToShowImageEvent.value =
                    NewsShowImageCarouselUi(targetItem.ui.imagesUriList!!, position)
            }

            else -> return
        }
    }

    fun showDetails(id: String) {
        val allNews = _news.value

        if (allNews != null) {
            val article = allArticles.firstOrNull {
                it.id == id
            }

            if (article != null) {
                val list = if (article.imageUrl != null) {
                    listOf(article.imageUrl)
                } else {
                    null
                }

                _goToDetailsEvent.value = DetailsUi(
                    id = article.id,
                    header = article.title ?: "",
                    body = article.description ?: "",
                    url = article.url,
                    imagesUriList = list,
                    date = dateUtils.getElapsedTime(article.publishedAt),
                    isFavorite = favouriteSet.contains(id),
                    userId = currentUser!!.id
                )
            }
        }
    }

    private fun getRequest(queryText: String?, user: Profile): NewsEverythingRequest {
        return if (queryText == null) {
            NewsEverythingRequest(
                page = page,
                pageSize = PAGE_SIZE,
                sources = user.sources,
                language = user.language
            )
        } else {
            NewsEverythingRequest(
                query = queryText,
                page = page,
                pageSize = PAGE_SIZE,
                language = user.language
            )
        }
    }

    private suspend fun getCurrentUser() {
        val profileJob = viewModelScope.async { profileRepository.getProfile() }
        val favouriteResultJob = viewModelScope.async { profileRepository.getFavourites() }

        val result = profileJob.await()
        val favouriteResult = favouriteResultJob.await()

        if (favouriteResult.isSuccess) {
            favouriteSet = favouriteResult.getOrNull()?.map { it.id }?.toMutableSet() ?: mutableSetOf()
        } else if (result.isFailure) {
            val exception = result.exceptionOrNull()
            if (exception != null) {
                Log.e("News", exception.stackTraceToString())
                _errorEvent.value = R.string.home_page_error
            }
        }

        if (result.isSuccess) {
            currentUser = result.getOrNull()
            if (currentUser != null) {
                _image.value = currentUser!!.imageUrl ?: ""
            }
        } else if (result.isFailure) {
            val exception = result.exceptionOrNull()
            if (exception != null) {
                Log.e("News", exception.stackTraceToString())
                _errorEvent.value = R.string.home_page_error
            }
        }
    }

    private fun mapArticleToItem(article: Article): IDelegateAdapterItem {
        val elapsed = dateUtils.getElapsedTime(article.publishedAt)
        val imageList = if (article.imageUrl != null) {
            listOf(article.imageUrl)
        } else {
            null
        }

        val newsUi = NewsUi(
            id = article.id,
            header = article.title ?: "",
            body = article.description ?: "",
            imagesUriList = imageList,
            date = elapsed,
            isFavorite = favouriteSet.contains(article.id)
        )

        return if (article.imageUrl != null) {
            NewsImageItem(newsUi)
        } else {
            NewsTextItem(newsUi)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}

