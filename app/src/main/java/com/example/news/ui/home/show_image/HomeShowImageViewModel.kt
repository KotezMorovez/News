package com.example.news.ui.home.show_image

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class HomeShowImageViewModel @Inject constructor(): ViewModel() {
    var isFirstLaunch = true
    var position: Int = 0
    var imageUrlList: Array<String> = arrayOf()
}