package com.example.news.ui.home.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.home.models.DetailsUi
import javax.inject.Inject

class HomeDetailsViewModelFactory @Inject constructor(
    private val repository: ProfileRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == HomeDetailsViewModel::class.java) {
            return HomeDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}