package com.example.news.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.auth.signup.SignUpViewModel
import javax.inject.Inject

class VerificationViewModelFactory @Inject constructor(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == VerificationViewModel::class.java) {
            return VerificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}