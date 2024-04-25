package com.example.news.ui.home.show_image

import androidx.lifecycle.ViewModel

class HomeShowImageViewModel: ViewModel() {
    var isFirstLaunch = true
    var position: Int = 0
    var imageUrlList: Array<String> = arrayOf()
}