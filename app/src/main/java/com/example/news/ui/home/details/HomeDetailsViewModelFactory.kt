package com.example.news.ui.home.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.ui.home.models.DetailsUi

class HomeDetailsViewModelFactory(
    private val news: DetailsUi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == HomeDetailsViewModel::class.java) {
            return HomeDetailsViewModel(news) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}