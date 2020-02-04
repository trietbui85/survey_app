package com.myapp.ui.main

import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    fun fetchSurveys(pageNumber: Int, forceReload: Boolean = false) {
        Timber.d("Start fetching survey of page $pageNumber")
    }

    fun loadNextPage() {
    }

}