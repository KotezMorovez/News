package com.example.news.ui.auth.verification

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentVerificationBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.common.BaseFragment
import com.example.news.ui.common.makeLinks
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class VerificationFragment : BaseFragment<FragmentVerificationBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<VerificationViewModel>
    private val viewModel: VerificationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[VerificationViewModel::class.java]
    }

    override fun createViewBinding(): FragmentVerificationBinding {
        return FragmentVerificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        with(viewBinding) {
            var spanTextList = linkAuthTextView.text.toString().split("?")
            var spannableString = makeLinks(
                linkAuthTextView.text.toString(),
                spanTextList[1].trim(),
                requireContext()
            ) {
                viewModel.logout()
            }
            linkAuthTextView.movementMethod = LinkMovementMethod.getInstance()
            linkAuthTextView.setText(spannableString, TextView.BufferType.SPANNABLE)

            spanTextList = linkLoginTextView.text.toString().split("?")
            spannableString = makeLinks(
                linkLoginTextView.text.toString(),
                spanTextList[1].trim(),
                requireContext()
            ) {
                viewModel.logout()
            }
            linkLoginTextView.movementMethod = LinkMovementMethod.getInstance()
            linkLoginTextView.setText(spannableString, TextView.BufferType.SPANNABLE)

            spanTextList = sendEmailTextView.text.toString().split("?")
            spannableString = makeLinks(
                sendEmailTextView.text.toString(),
                spanTextList[1].trim(),
                requireContext()
            ) {
                viewModel.sendEmail()
            }
            sendEmailTextView.movementMethod = LinkMovementMethod.getInstance()
            sendEmailTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    override fun observeData() {
        viewModel.successEvent.observe(this) {
            this@VerificationFragment.findNavController()
                .navigate(R.id.action_verificationFragment_to_loginFragment)
        }

        viewModel.errorEvent.observe(this) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.sendEmailTextView,
                resources.getText(R.string.verification_send_email_failure),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.logoutSuccessEvent.observe(this) {
            this@VerificationFragment.findNavController()
                .navigate(R.id.action_verificationFragment_to_loginFragment)
        }
    }
}