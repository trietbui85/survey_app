package com.myapp.data.remote

import com.myapp.data.remote.model.AccessTokenRequest
import com.myapp.data.remote.model.AccessTokenResponse
import timber.log.Timber

interface TokenRemoteDataSource {

  suspend fun refreshAccessToken(): AccessTokenResponse
}

class TokenRemoteDataSourceImpl(
  private val tokenApiService: TokenApiService,
  grantType: String,
  username: String,
  password: String
) : TokenRemoteDataSource {
  private val defaultTokenRequest: AccessTokenRequest = AccessTokenRequest(
    grantType, username, password
  )

  override suspend fun refreshAccessToken(): AccessTokenResponse {
    Timber.d("defaultTokenRequest = $defaultTokenRequest")
    return tokenApiService.refreshAccessToken(defaultTokenRequest)
  }

}