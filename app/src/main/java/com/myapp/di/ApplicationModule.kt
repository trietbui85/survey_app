package com.myapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.myapp.data.local.SurveyLocalDataSource
import com.myapp.data.local.SurveyLocalDataSourceImpl
import com.myapp.data.local.TokenLocalDataSource
import com.myapp.data.local.TokenLocalDataSourceImpl
import com.myapp.data.local.db.SurveyDatabase
import com.myapp.data.remote.*
import com.myapp.data.repo.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ApplicationModuleBinds::class, NetworkModule::class])
class ApplicationModule {

    @Singleton
    @Provides
    fun provideAccountAuthenticator(accountRepository: AccountRepository): TokenAuthenticator {
        return TokenAuthenticator(accountRepository)
    }

    @Singleton
    @Provides
    fun provideTokenLocalDataSource(
        pref: SharedPreferences,
        mapper: AccountDataMapper
    ): TokenLocalDataSource {
        return TokenLocalDataSourceImpl(pref, mapper)
    }

    @Singleton
    @Provides
    fun provideSurveyLocalDataSource(database: SurveyDatabase): SurveyLocalDataSource {
        return SurveyLocalDataSourceImpl(database)
    }

    @Singleton
    @Provides
    fun provideSurveyRemoteDataSource(surveyApiService: SurveyApiService): SurveyRemoteDataSource {
        return SurveyRemoteDataSourceImpl(surveyApiService)
    }

    @Singleton
    @Provides
    fun provideTokenRemoteDataSource(tokenApiService: TokenApiService): TokenRemoteDataSource {
        return TokenRemoteDataSourceImpl(tokenApiService)
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): SurveyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SurveyDatabase::class.java,
            "${SurveyDatabase::class.java.simpleName}.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("surveys", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAccountDataMapper(gson: Gson): AccountDataMapper = AccountDataMapper(gson)

    @Singleton
    @Provides
    @Named("MainDispatcher")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    @Named("IoDispatcher")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}

@ExperimentalCoroutinesApi
@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindAccountRepository(repo: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    abstract fun bindSurveyRepository(repo: SurveyRepositoryImpl): SurveyRepository
}