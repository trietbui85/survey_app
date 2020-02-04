package com.myapp.di

import android.content.Context
import com.myapp.ui.SurveyApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Main component for the application.
 */
@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        SurveysModule::class,
        DetailModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<SurveyApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}