package com.example.news.ui.home.show_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject

class HomeShowImageViewModelFactory @Inject constructor() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == HomeShowImageViewModel::class.java) {
            return HomeShowImageViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}