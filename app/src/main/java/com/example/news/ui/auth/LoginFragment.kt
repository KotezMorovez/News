package com.example.news.ui.auth

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentLoginBinding
import com.example.news.ui.common.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel = LoginViewModel()

    override fun createViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun initUi(isFirstLaunch: Boolean) {
        with(viewBinding) {
            emailEditText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    viewModel.setEmail(emailEditText.text.toString())
                }
            }

            passwordEditText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    viewModel.setPassword(passwordEditText.text.toString())
                }
            }

            loginButton.setOnClickListener {
                clearAllFocus()
                viewModel.loginUser.value!!.isValidEmail
                viewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )

            }

            val spanTextList = loginLinkTextView.text.toString().split("?")
            val spannableString = makeLinks(
                loginLinkTextView.text.toString(),
                spanTextList[1].trim(),
                View.OnClickListener{
                    this@LoginFragment.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                })
            loginLinkTextView.movementMethod = LinkMovementMethod.getInstance()
            loginLinkTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    private fun makeLinks(text: String, phrase: String, listener: View.OnClickListener): SpannableString {
        val spannableString = SpannableString(text)
        val start = text.indexOf(phrase)
        val end = start + phrase.length

        val clickableSpan = object: ClickableSpan() {

            override fun updateDrawState(ds: TextPaint){
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
        viewModel.loginUser.observe(viewLifecycleOwner, Observer {
            with(viewBinding) {
                emailEditText.setText(it.email)
                if (!it.isValidEmail){
                    emailContainer.error = resources.getText(R.string.login_email_error)
                } else {
                    emailContainer.error = null
                }

                passwordEditText.setText(it.password)
                if (!it.isValidPassword){
                    passwordContainer.error = resources.getText(R.string.login_password_error)
                } else {
                    passwordContainer.error = null
                }
            }
        })
    }

    override fun onDestroyView() {
        clearAllFocus()
        super.onDestroyView()
    }
}