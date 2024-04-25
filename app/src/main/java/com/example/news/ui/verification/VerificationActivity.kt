package com.example.news.ui.verification

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.databinding.ActivityVerificationBinding
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.common.makeLinks
import com.google.android.material.snackbar.Snackbar

class VerificationActivity : AppCompatActivity() {
    private lateinit var viewModel: VerificationViewModel
    private lateinit var viewBinding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_News)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[VerificationViewModel::class.java]

        viewBinding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initUi()
        observeData()
    }

    private fun initUi() {
        with(viewBinding) {
            var spanTextList = linkAuthTextView.text.toString().split("?")
            var spannableString = makeLinks(
                linkAuthTextView.text.toString(),
                spanTextList[1].trim(),
                this@VerificationActivity
            ) {
                viewModel.logout()
            }
            linkAuthTextView.movementMethod = LinkMovementMethod.getInstance()
            linkAuthTextView.setText(spannableString, TextView.BufferType.SPANNABLE)

            spanTextList = sendEmailTextView.text.toString().split("?")
            spannableString = makeLinks(
                sendEmailTextView.text.toString(),
                spanTextList[1].trim(),
                this@VerificationActivity
            ) {
                viewModel.sendEmail()
            }
            sendEmailTextView.movementMethod = LinkMovementMethod.getInstance()
            sendEmailTextView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    private fun observeData() {
        viewModel.successEvent.observe(this) {
            val snackBar = Snackbar.make(
                this,
                viewBinding.sendEmailTextView,
                resources.getText(R.string.verification_send_email_success),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.errorEvent.observe(this) {
            val snackBar = Snackbar.make(
                this,
                viewBinding.sendEmailTextView,
                resources.getText(R.string.verification_send_email_failure),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }

        viewModel.logoutSuccessEvent.observe(this) {
            val intent = Intent(this@VerificationActivity, AuthActivity::class.java)
            startActivity(intent)
            this@VerificationActivity.finish()
        }
    }
}