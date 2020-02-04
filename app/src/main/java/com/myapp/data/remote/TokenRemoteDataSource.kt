package com.myapp.data.remote

import com.myapp.BuildConfig
import com.myapp.data.remote.model.AccessTokenRequest
import com.myapp.data.remote.model.AccessTokenResponse
import timber.log.Timber

interface TokenRemoteDataSource {

    suspend fun refreshAccessToken(): AccessTokenResponse
}

class TokenRemoteDataSourceImpl(
    private val tokenApiService: TokenApiService,
    private val defaultTokenRequest: AccessTokenRequest = AccessTokenRequest(
        grantType = BuildConfig.ACCOUNT_GRANT_TYPE,
        username = BuildConfig.ACCOUNT_DEFAULT_USERNAME,
        password = BuildConfig.ACCOUNT_DEFAULT_PASSWORD
    )
) : TokenRemoteDataSource {
    override suspend fun refreshAccessToken(): AccessTokenResponse {
        Timber.d("defaultTokenRequest = $defaultTokenRequest")
        return tokenApiService.refreshAccessToken(defaultTokenRequest)
    }

}