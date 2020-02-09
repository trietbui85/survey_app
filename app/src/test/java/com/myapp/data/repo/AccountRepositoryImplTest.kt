package com.myapp.data.repo

import com.myapp.data.local.TokenLocalDataSource
import com.myapp.data.remote.TokenRemoteDataSource
import com.myapp.utils.TestData.testTokenEntity
import com.myapp.utils.TestData.testTokenItem
import com.myapp.utils.TestData.testTokenResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AccountRepositoryImplTest {

  private val remoteDataSource: TokenRemoteDataSource = mock()
  private val localDataSource: TokenLocalDataSource = mock()
  private val mapper: AccountDataMapper = mock()

  private lateinit var accountRepository: AccountRepositoryImpl

  @Before
  fun setUp() {
    accountRepository = AccountRepositoryImpl(remoteDataSource, localDataSource, mapper)
  }

  @Test
  fun getTokenFromCache_HappyCase() {
    runBlocking {
      whenever(localDataSource.loadTokenFromCache()) doReturn testTokenEntity
      accountRepository.getTokenFromCache()

      verify(localDataSource).loadTokenFromCache()
      verify(mapper).fromAccessTokenEntity(testTokenEntity)
    }
  }

  @Test
  fun refreshToken_HappyCase() {
    runBlocking {
      whenever(remoteDataSource.refreshAccessToken()) doReturn testTokenResponse
      whenever(mapper.fromAccessTokenResponse(testTokenResponse)) doReturn testTokenItem
      whenever(mapper.toAccessTokenEntity(testTokenItem)) doReturn testTokenEntity

      accountRepository.refreshToken()

      verify(localDataSource).removeTokenFromCache()
      verify(mapper).fromAccessTokenResponse(testTokenResponse)
      verify(mapper).toAccessTokenEntity(testTokenItem)
      verify(localDataSource).saveTokenToCache(testTokenEntity)
    }
  }
}