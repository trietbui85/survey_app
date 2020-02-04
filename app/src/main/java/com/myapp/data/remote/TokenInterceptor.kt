package com.myapp.data.remote

import com.myapp.data.repo.AccountRepository
import com.myapp.utils.toBearerToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class TokenInterceptor(
    private val accountRepository: AccountRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenItem = runBlocking {
            accountRepository.getTokenFromCache()
        }

        return if (tokenItem == null) {
            chain.proceed(chain.request())
        } else {
            Timber.d("Add token to request: ${chain.request()}")
            val authenticatedRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", tokenItem.toBearerToken())
                .build()
            chain.proceed(authenticatedRequest)
        }
    }
}