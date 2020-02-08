package com.myapp.ui

import com.myapp.BuildConfig
import com.myapp.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
open class SurveyApp : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

    return DaggerApplicationComponent.factory()
      .create(applicationContext)
  }

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    Timber.d("Start SURVEY Application")
  }
}