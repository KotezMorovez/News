package com.example.news.ui.home.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.ui.common.DateUtils

class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == HomeViewModel::class.java) {
            return HomeViewModel(DateUtils(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}