package com.example.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.news.common.ui.GlobalConstants
import com.example.news.common.ui.Languages
import com.example.news.data.mapper.toEntity
import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.data.service.AuthService
import com.example.news.data.service.FirebaseService
import com.example.news.domain.model.AuthUser
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.model.auth.SignUpRequest
import com.example.news.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.MockitoKotlinException
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AuthRepositoryTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val authService: AuthService = mock()
    private val userService: FirebaseService = mock()
    private val repository: AuthRepository = AuthRepositoryImpl(authService, userService)

    private val id = "1"
    private val name = "Test"
    private val email = "test@test.test"
    private val password = "123123Az!"
    private val exception = IOException()

    @Test
    fun `verify checking authorized user from auth service`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
        }

        // WHEN
        val result = runBlocking {
            repository.isUserAuthorized()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            assert(result)
        }
    }

    @Test
    fun `verify checking not authorized user from auth service`() {
        // GIVEN
        val id = null
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
        }

        // WHEN
        val result = runBlocking {
            repository.isUserAuthorized()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            assert(!result)
        }
    }

    @Test
    fun `verify checking verified user from auth service`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getUserVerificationStatus()).thenReturn(true)
        }

        // WHEN
        val result = runBlocking {
            repository.isUserVerified()
        }

        // THEN
        runBlocking {
            verify(authService).getUserVerificationStatus()
            assert(result)
        }
    }

    @Test
    fun `verify checking not verified user from auth service`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getUserVerificationStatus()).thenReturn(false)
        }

        // WHEN
        val result = runBlocking {
            repository.isUserVerified()
        }

        // THEN
        runBlocking {
            verify(authService).getUserVerificationStatus()
            assert(!result)
        }
    }

    @Test
    fun `verify getting not null user ID from auth service`() {
        // GIVEN
        val expectation = id
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
        }

        // WHEN
        val result = runBlocking {
            repository.getUserId()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            assertEquals(result, expectation)
        }
    }

    @Test
    fun `verify getting null user ID from auth service`() {
        // GIVEN
        val id = null
        val expectation = null
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
        }

        // WHEN
        val result = runBlocking {
            repository.getUserId()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            assertEquals(result, expectation)
        }
    }

    @Test
    fun `verify successful sending of verification email`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.sendVerificationEmail()).thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.sendVerificationEmail()
        }

        // THEN
        runBlocking {
            verify(authService).sendVerificationEmail()
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure sending of verification email`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.sendVerificationEmail())
                .thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.sendVerificationEmail()
        }

        // THEN
        runBlocking {
            verify(authService).sendVerificationEmail()
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful login of the registered user`() {
        // GIVEN
        val loginRequest = LoginRequest(email, password)

        val user = mock<FirebaseUser>()
        Mockito.`when`(user.uid).thenReturn(id)
        Mockito.`when`(user.email).thenReturn(email)

        runBlocking {
            Mockito.`when`(authService.loginUser(email, password)).thenReturn(Result.success(user))
        }

        // WHEN
        val result = runBlocking {
            repository.loginUser(loginRequest)
        }

        // THEN
        runBlocking {
            verify(authService).loginUser(email, password)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure login of the not registered user`() {
        // GIVEN
        val loginRequest = LoginRequest(email, password)

        runBlocking {
            Mockito.`when`(authService.loginUser(email, password))
                .thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.loginUser(loginRequest)
        }

        // THEN
        runBlocking {
            verify(authService).loginUser(email, password)
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful sign up user`() {
        // GIVEN
        val signUpRequest = SignUpRequest(
            name = name,
            email = email,
            password = password,
            language = Languages.EN.toString(),
            sources = GlobalConstants.DEFAULT_SOURCES
        )

        runBlocking {
            Mockito.`when`(authService.registerUser(signUpRequest.toEntity()))
                .thenReturn(Result.success(id))
            Mockito.`when`(userService.registerUser(signUpRequest.toEntity(), id))
                .thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.registerUser(signUpRequest)
        }

        // THEN
        runBlocking {
            verify(authService).registerUser(signUpRequest.toEntity())
            verify(userService).registerUser(signUpRequest.toEntity(), id)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify auth service failure when sign up user`() {
        // GIVEN
        val signUpRequest = SignUpRequest(
            name = name,
            email = email,
            password = password,
            language = Languages.EN.toString(),
            sources = GlobalConstants.DEFAULT_SOURCES
        )

        runBlocking {
            Mockito.`when`(authService.registerUser(signUpRequest.toEntity()))
                .thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.registerUser(signUpRequest)
        }

        // THEN
        runBlocking {
            verify(authService).registerUser(signUpRequest.toEntity())
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify user service failure when sign up user`() {
        // GIVEN
        val signUpRequest = SignUpRequest(
            name = name,
            email = email,
            password = password,
            language = Languages.EN.toString(),
            sources = GlobalConstants.DEFAULT_SOURCES
        )

        runBlocking {
            Mockito.`when`(authService.registerUser(signUpRequest.toEntity()))
                .thenReturn(Result.success(id))
            Mockito.`when`(userService.registerUser(signUpRequest.toEntity(), id))
                .thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.registerUser(signUpRequest)
        }

        // THEN
        runBlocking {
            verify(authService).registerUser(signUpRequest.toEntity())
            verify(userService).registerUser(signUpRequest.toEntity(), id)
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful logout user`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.logoutUser()).thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.logoutUser()
        }

        // THEN
        runBlocking {
            verify(authService).logoutUser()
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure logout user`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.logoutUser()).thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.logoutUser()
        }

        // THEN
        runBlocking {
            verify(authService).logoutUser()
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful reset password`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.resetPassword(email)).thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.resetPassword(email)
        }

        // THEN
        runBlocking {
            verify(authService).resetPassword(email)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure reset password`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.resetPassword(email)).thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.resetPassword(email)
        }

        // THEN
        runBlocking {
            verify(authService).resetPassword(email)
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful change password`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.changePassword(password, password))
                .thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.changePassword(password, password)
        }

        // THEN
        runBlocking {
            verify(authService).changePassword(password, password)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure change password`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.changePassword(password, password))
                .thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.changePassword(password, password)
        }

        // THEN
        runBlocking {
            verify(authService).changePassword(password, password)
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify successful account deletion`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
            Mockito.`when`(userService.deleteAccount(id)).thenReturn(Result.success(Unit))
            Mockito.`when`(authService.deleteAccount()).thenReturn(Result.success(Unit))
        }

        // WHEN
        val result = runBlocking {
            repository.deleteAccount()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            verify(authService).deleteAccount()
            verify(userService).deleteAccount(id)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `verify failure getting id when account deletion`() {
        // GIVEN
        val id = null
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
        }

        // WHEN
        val result = runBlocking {
            repository.deleteAccount()
        }

        // THEN
        runBlocking {
            verify(authService, Mockito.never()).deleteAccount()
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify failure of account deletion on user service`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
            Mockito.`when`(userService.deleteAccount(id)).thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.deleteAccount()
        }

        // THEN
        runBlocking {
            verify(userService).deleteAccount(id)
            verify(authService).getCurrentUserId()
            assert(result.isFailure)
        }
    }

    @Test
    fun `verify failure of account deletion on auth service`() {
        // GIVEN
        runBlocking {
            Mockito.`when`(authService.getCurrentUserId()).thenReturn(id)
            Mockito.`when`(userService.deleteAccount(id)).thenReturn(Result.success(Unit))
            Mockito.`when`(authService.deleteAccount()).thenReturn(Result.failure(exception))
        }

        // WHEN
        val result = runBlocking {
            repository.deleteAccount()
        }

        // THEN
        runBlocking {
            verify(authService).getCurrentUserId()
            verify(authService).deleteAccount()
            verify(userService).deleteAccount(id)
            assert(result.isFailure)
        }
    }
}