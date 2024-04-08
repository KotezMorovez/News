package com.example.news.ui.auth.reset_password

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.databinding.FragmentForgetPasswordBinding
import com.example.news.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar

class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    private lateinit var viewModel: ForgetPasswordViewModel

    override fun createViewBinding(): FragmentForgetPasswordBinding {
        return FragmentForgetPasswordBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel = ViewModelProvider(this)[ForgetPasswordViewModel::class.java]

        with(viewBinding) {
            forgetPasswordButton.setOnClickListener {
                viewModel.sendEmail(forgetPasswordEmailEditText.text.toString())
            }
        }
    }

    override fun observeData() {
        viewModel.successEvent.observe(viewLifecycleOwner) {
            with(viewBinding) {
                resetPasswordMessage.visibility = VISIBLE
                resetPasswordRequestField.visibility = GONE
            }
        }

        viewModel.failureEvent.observe(viewLifecycleOwner) {
            when(it!!) {
                ForgetPasswordError.VALIDATION_ERROR -> {
                    viewBinding.forgetPasswordEmailContainer.error = resources.getText(R.string.forget_password_email_error)
                }
                ForgetPasswordError.SERVICE_ERROR -> {
                    val snackBar = Snackbar.make(
                        requireContext(),
                        viewBinding.forgetPasswordButton,
                        resources.getText(R.string.forget_password_server_error),
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }
            }
        }
    }
}