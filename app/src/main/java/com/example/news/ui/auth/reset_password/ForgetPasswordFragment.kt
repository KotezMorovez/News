package com.example.news.ui.auth.reset_password

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.news.R
import com.example.news.databinding.FragmentForgetPasswordBinding
import com.example.news.di.AppComponentHolder
import com.example.news.ui.auth.login.LoginViewModelFactory
import com.example.news.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {

    @Inject
    lateinit var viewModelFactory: ForgetPasswordViewModelFactory

    private val viewModel: ForgetPasswordViewModel by viewModels { viewModelFactory }

    override fun createViewBinding(): FragmentForgetPasswordBinding {
        return FragmentForgetPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
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