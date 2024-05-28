package com.example.news.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.di.AppComponent
import com.example.news.di.AppComponentHolder
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.home.HomeActivity
import com.example.news.ui.verification.VerificationActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)

        setTheme(R.style.Theme_News)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeData()

        viewModel.check()
    }

    private fun observeData() {
        viewModel.checkSuccessEvent.observe(this) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        viewModel.checkVerificationEvent.observe(this) {
            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        viewModel.checkFailureEvent.observe(this) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}