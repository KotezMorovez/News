package com.example.news.ui.home.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.ProfileRepository
import javax.inject.Inject

class FavouriteViewModelFactory @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == FavouriteViewModel::class.java) {
            return FavouriteViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}