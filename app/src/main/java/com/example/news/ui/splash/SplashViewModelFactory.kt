package com.example.news.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import javax.inject.Inject

class SplashViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == SplashViewModel::class.java) {
            return SplashViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}