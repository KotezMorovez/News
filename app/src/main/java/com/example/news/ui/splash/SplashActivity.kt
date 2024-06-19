package com.example.news.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.databinding.ActivitySplashBinding
import com.example.news.di.AppComponentHolder
import com.example.news.di.ViewModelFactory
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.home.HomeActivity
import com.example.news.ui.verification.VerificationActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SplashViewModel>
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        val viewBinding = createViewBinding()
        setTheme(R.style.Theme_News)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        observeData()

        if(Build.VERSION.SDK_INT >= 29) {
            viewBinding.root.isForceDarkAllowed = false
        }

        viewModel.check()
    }

    fun createViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
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