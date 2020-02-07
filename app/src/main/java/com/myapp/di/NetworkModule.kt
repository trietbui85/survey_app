package com.myapp.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myapp.BuildConfig
import com.myapp.data.remote.SurveyApiService
import com.myapp.data.remote.TokenApiService
import com.myapp.data.remote.TokenAuthenticator
import com.myapp.data.remote.TokenInterceptor
import com.myapp.data.repo.AccountRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class NetworkModule {

  @Singleton
  @Provides
  @Named("NumOfItemPerPage")
  fun providerNumOfItemPerPage(): Int = 5

  @Singleton
  @Provides
  @Named("EndpointUrl")
  fun providerEndpointUrl(): String = BuildConfig.ENDPOINT_URL

  @Singleton
  @Provides
  fun provideSurveyApiService(
    gson: Gson,
    @Named("SurveyHttpClient") okHttpClient: OkHttpClient,
    @Named("EndpointUrl") endpointUrl: String
  ): SurveyApiService {
    return Retrofit.Builder()
        .baseUrl(endpointUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(SurveyApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideTokenApiService(
    gson: Gson,
    @Named("TokenHttpClient") okHttpClient: OkHttpClient,
    @Named("EndpointUrl") endpointUrl: String
  ): TokenApiService {
    return Retrofit.Builder()
        .baseUrl(endpointUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(TokenApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideGson(): Gson = GsonBuilder().setFieldNamingPolicy(
      FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
  ).create()

  @Singleton
  @Provides
  @Named("SurveyHttpClient")
  fun provideSurveyOkHttpClient(accountRepository: AccountRepository): OkHttpClient {
    val logInterceptor = HttpLoggingInterceptor().also {
      it.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient.Builder()
        .also {
          it.addInterceptor(TokenInterceptor(accountRepository))
          it.authenticator(TokenAuthenticator(accountRepository))
          it.addInterceptor(logInterceptor)
        }
        .build()
  }

  @Singleton
  @Provides
  @Named("TokenHttpClient")
  fun provideTokenOkHttpClient(): OkHttpClient {
    val logInterceptor = HttpLoggingInterceptor().also {
      it.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient.Builder()
        .also {
          it.addInterceptor(logInterceptor)
        }
        .build()
  }
}