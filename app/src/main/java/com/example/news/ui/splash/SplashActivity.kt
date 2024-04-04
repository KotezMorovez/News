package com.example.news.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        super.onCreate(savedInstanceState, persistentState)

        val result = viewModel.check()
        if (result){} else {}
    }
}