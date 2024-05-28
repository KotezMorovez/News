package com.example.news.ui.profile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.auth.signup.SignUpViewModel
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == ProfileViewModel::class.java) {
            return ProfileViewModel(profileRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}