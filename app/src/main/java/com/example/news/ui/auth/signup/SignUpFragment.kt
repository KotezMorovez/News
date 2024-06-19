package com.example.news.ui.auth.signup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentSignupBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.makeLinks
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SignUpViewModel>
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun createViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        with(viewBinding) {
            nameEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.setName(nameEditText.text.toString())
                }
            }

            emailEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.setEmail(emailEditText.text.toString())
                }
            }

            passwordEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.setPassword(passwordEditText.text.toString())
                }
            }

            signUpButton.setOnClickListener {
                clearAllFocus()
                viewModel.signUp(
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }

            val spanTextList = signUpLinkTextView.text.toString().split("?")
            val spannableString = makeLinks(
                signUpLinkTextView.text.toString(),
                spanTextList[1].trim(),
                requireContext()
            ) {
                this@SignUpFragment.findNavController()
                    .navigate(R.id.action_signUpFragment_to_loginFragment)
            }
            signUpLinkTextView.movementMethod = LinkMovementMethod.getInstance()
            signUpLinkTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    private fun clearAllFocus() {
        with(viewBinding) {
            nameEditText.clearFocus()
            emailEditText.clearFocus()
            passwordEditText.clearFocus()
        }
    }

    override fun observeData() {
        viewModel.signUpUser.observe(viewLifecycleOwner) {
            with(viewBinding) {
                nameEditText.setText(it.name)
                if (!it.isValidName) {
                    nameContainer.error = resources.getText(R.string.sign_up_name_error)
                } else {
                    nameContainer.error = null
                }

                emailEditText.setText(it.email)
                if (!it.isValidEmail) {
                    emailContainer.error = resources.getText(R.string.sign_up_email_error)
                } else {
                    emailContainer.error = null
                }

                passwordEditText.setText(it.password)
                if (!it.isValidPassword) {
                    passwordContainer.error = resources.getText(R.string.sign_up_password_error)
                } else {
                    passwordContainer.error = null
                }
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.signUpButton,
                resources.getText(R.string.sign_up_toast_error),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.signUpSuccessEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.signUpButton,
                resources.getText(R.string.sign_up_verification_toast),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()

            this@SignUpFragment.findNavController()
                .navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        clearAllFocus()
        super.onDestroyView()
    }
}