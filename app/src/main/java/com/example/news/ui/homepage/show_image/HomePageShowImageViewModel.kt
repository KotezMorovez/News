package com.example.news.ui.homepage.show_image

import androidx.lifecycle.ViewModel

class HomePageShowImageViewModel: ViewModel() {
    var isFirstLaunch = true
    var position: Int = 0
    var imageUrlList: Array<String> = arrayOf()
}