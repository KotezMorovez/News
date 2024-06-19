package com.example.news

import android.text.SpannableString
import android.text.style.ClickableSpan
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.text.getSpans
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.news.di.TestApp
import com.example.news.ui.auth.signup.SignUpFragment
import com.google.firebase.FirebaseApp
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, application = TestApp::class)
class SignUpFragmentTest {
    private val name = "Test"
    private val email = "test@test.test"
    private val password = "123123Az!"
    private val mockNavController = mock(NavController::class.java)

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun `verify success sign up action`() {
        val scenario = launchFragmentInContainer<SignUpFragment>(
            fragmentArgs = null,
            themeResId = R.style.Theme_News_Auth,
            initialState = Lifecycle.State.STARTED
        )

        scenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever{
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
            }

            fragment.viewBinding.nameEditText.setText(name)
            fragment.viewBinding.emailEditText.setText(email)
            fragment.viewBinding.passwordEditText.setText(password)

            fragment.viewBinding.signUpButton.performClick()
        }
        verify(mockNavController).navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    @Test
    fun `verify success login nav action`() {
        val scenario = launchFragmentInContainer<SignUpFragment>(
            fragmentArgs = null,
            themeResId = R.style.Theme_News_Auth,
            initialState = Lifecycle.State.STARTED
        )

        scenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever{
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
            }
            val spannableString = fragment.viewBinding.signUpLinkTextView.text as SpannableString
            val spanBtn = spannableString.getSpans<ClickableSpan>(0, spannableString.length)[0]

            spanBtn.onClick(fragment.viewBinding.signUpLinkTextView)
        }
        verify(mockNavController).navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    @Test
    fun `verify failure when sign up with an empty name field`() {
        val scenario = launchFragmentInContainer<SignUpFragment>(
            fragmentArgs = null,
            themeResId = R.style.Theme_News_Auth,
            initialState = Lifecycle.State.STARTED
        )

        scenario.onFragment { fragment ->
            val expectation = fragment.requireContext().getString(R.string.sign_up_name_error)

            fragment.viewBinding.signUpButton.performClick()
            val actual = fragment.viewBinding.nameContainer.error.toString()

            assertEquals("Wrong answer", expectation, actual)
        }
    }

    @Test
    fun `verify failure when sign up with an empty email field`() {
        val scenario = launchFragmentInContainer<SignUpFragment>(
            fragmentArgs = null,
            themeResId = R.style.Theme_News_Auth,
            initialState = Lifecycle.State.STARTED
        )

        scenario.onFragment { fragment ->
            val expectation = fragment.requireContext().getString(R.string.sign_up_email_error)

            fragment.viewBinding.signUpButton.performClick()
            val actual = fragment.viewBinding.emailContainer.error.toString()

            assertEquals("Wrong answer", expectation, actual)
        }
    }

    @Test
    fun `verify failure when sign up with an empty password field`() {
        val scenario = launchFragmentInContainer<SignUpFragment>(
            fragmentArgs = null,
            themeResId = R.style.Theme_News_Auth,
            initialState = Lifecycle.State.STARTED
        )

        scenario.onFragment { fragment ->
            val expectation = fragment.requireContext().getString(R.string.sign_up_password_error)

            fragment.viewBinding.signUpButton.performClick()
            val actual = fragment.viewBinding.passwordContainer.error.toString()

            assertEquals("Wrong answer", expectation, actual)
        }
    }
}