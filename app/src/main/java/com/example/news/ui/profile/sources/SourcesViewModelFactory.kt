package com.example.news.ui.profile.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.auth.signup.SignUpViewModel
import javax.inject.Inject

class SourcesViewModelFactory @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == SourcesViewModel::class.java) {
            return SourcesViewModel(profileRepository, newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}