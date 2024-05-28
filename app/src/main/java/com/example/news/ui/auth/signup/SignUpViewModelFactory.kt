package com.example.news.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.auth.login.LoginViewModel
import javax.inject.Inject

class SignUpViewModelFactory @Inject constructor(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == SignUpViewModel::class.java) {
            return SignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}