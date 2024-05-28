package com.example.news.ui.home.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.common.DateUtils
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val dateUtils: DateUtils,
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == HomeViewModel::class.java) {
            return HomeViewModel(dateUtils, newsRepository, profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}