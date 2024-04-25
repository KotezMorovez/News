package com.example.news.ui.auth.signup

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentSignupBinding
import com.example.news.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    private lateinit var viewModel: SignUpViewModel
    override fun createViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
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
                spanTextList[1].trim()
            ) {
                this@SignUpFragment.findNavController()
                    .navigate(R.id.action_signUpFragment_to_loginFragment)
            }
            signUpLinkTextView.movementMethod = LinkMovementMethod.getInstance()
            signUpLinkTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    private fun makeLinks(
        text: String,
        phrase: String,
        listener: View.OnClickListener
    ): SpannableString {
        val spannableString = SpannableString(text)
        val start = text.indexOf(phrase)
        val end = start + phrase.length

        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(R.color.white, null)
                ds.isUnderlineText = false
            }

            override fun onClick(v: View) {
                listener.onClick(v)
            }
        }

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
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