package com.example.news.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.ui.auth.AuthActivity
import com.example.news.ui.homepage.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_News)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
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

        viewModel.checkFailureEvent.observe(this) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}