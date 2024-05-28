package com.example.news.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.example.news.di.AppComponentHolder

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}