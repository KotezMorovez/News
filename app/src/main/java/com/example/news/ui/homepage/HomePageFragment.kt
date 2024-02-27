package com.example.news.ui.homepage

import android.content.Context.SEARCH_SERVICE
import android.view.Menu
import android.view.MenuInflater
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import com.example.news.R
import com.example.news.databinding.FragmentHomePageBinding
import com.example.news.ui.common.BaseFragment

class HomePageFragment: BaseFragment<FragmentHomePageBinding>() {
    override fun createViewBinding(): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(layoutInflater)
    }

    override fun initUi(isFirstLaunch: Boolean) {
        with(viewBinding){

        }
    }


    override fun observeData() {
        TODO("Not yet implemented")
    }
}