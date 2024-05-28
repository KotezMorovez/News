package com.example.news.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.ProfileRepository
import com.example.news.ui.auth.signup.SignUpViewModel
import javax.inject.Inject

class ProfileEditViewModelFactory @Inject constructor(
    private val userRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass == ProfileEditViewModel::class.java) {
            return ProfileEditViewModel(userRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}