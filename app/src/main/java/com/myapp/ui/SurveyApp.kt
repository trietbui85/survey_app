package com.myapp.ui

import android.app.Application
import com.myapp.BuildConfig
import timber.log.Timber

open class SurveyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        Timber.d("Start SURVEY Application")
    }
}