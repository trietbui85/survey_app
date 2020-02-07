package com.myapp.data.repo

import com.myapp.data.local.TokenLocalDataSource
import com.myapp.data.remote.TokenRemoteDataSource
import javax.inject.Inject

interface AccountRepository {
  suspend fun getTokenFromCache(): AccessTokenItem?

  suspend fun refreshToken(): AccessTokenItem?
}

class AccountRepositoryImpl @Inject constructor(
  private val remoteDataSource: TokenRemoteDataSource,
  private val localDataSource: TokenLocalDataSource,
  private val mapper: AccountDataMapper
) : AccountRepository {
  override suspend fun getTokenFromCache(): AccessTokenItem? {
    val tokenEntity = localDataSource.loadTokenFromCache() ?: return null
    return mapper.fromAccessTokenEntity(tokenEntity)
  }

  override suspend fun refreshToken(): AccessTokenItem? {
    return try {
      localDataSource.removeTokenFromCache()

      val tokenResponse = remoteDataSource.refreshAccessToken()

      val tokenItem = mapper.fromAccessTokenResponse(tokenResponse)

      val tokenEntity = mapper.toAccessTokenEntity(tokenItem)
      localDataSource.saveTokenToCache(tokenEntity)

      tokenItem
    } catch (e: Exception) {
      null
    }

  }

}