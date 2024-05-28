package com.example.news.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.example.news.di.AppComponentHolder

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}