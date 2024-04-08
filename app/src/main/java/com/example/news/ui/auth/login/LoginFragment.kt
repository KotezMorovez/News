package com.example.news.ui.auth.login

import android.content.Intent
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
import com.example.news.databinding.FragmentLoginBinding
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.homepage.HomeActivity
import com.google.android.material.snackbar.Snackbar

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private lateinit var viewModel: LoginViewModel

    override fun createViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        with(viewBinding) {
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

            loginForgetPassword.setOnClickListener {
                this@LoginFragment.findNavController()
                    .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }

            loginButton.setOnClickListener {
                clearAllFocus()
                viewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }

            val spanTextList = loginLinkTextView.text.toString().split("?")
            val spannableString = makeLinks(
                loginLinkTextView.text.toString(),
                spanTextList[1].trim()
            ) {
                this@LoginFragment.findNavController()
                    .navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            loginLinkTextView.movementMethod = LinkMovementMethod.getInstance()
            loginLinkTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
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
            emailEditText.clearFocus()
            passwordEditText.clearFocus()
        }
    }

    override fun observeData() {
        viewModel.loginUser.observe(viewLifecycleOwner) {
            with(viewBinding) {
                emailEditText.setText(it.email)
                if (!it.isValidEmail) {
                    emailContainer.error = resources.getText(R.string.login_email_error)
                } else {
                    emailContainer.error = null
                }

                passwordEditText.setText(it.password)
                if (!it.isValidPassword) {
                    passwordContainer.error = resources.getText(R.string.login_password_error)
                } else {
                    passwordContainer.error = null
                }
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.loginButton,
                resources.getText(R.string.login_toast_error),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.loginSuccessEvent.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        clearAllFocus()
        super.onDestroyView()
    }
}