package com.myapp.data.repo

import com.myapp.data.local.TokenLocalDataSource
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.remote.TokenRemoteDataSource
import com.myapp.data.remote.model.AccessTokenResponse
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

    private val tokenEntity = AccessTokenEntity(
        accessToken = "token_data"
    )
    private val tokenResponse = AccessTokenResponse(
        accessToken = "token_data"
    )
    private val tokenItem = AccessTokenItem(
        accessToken = "token_data"
    )

    @Before
    fun setUp() {
        accountRepository = AccountRepositoryImpl(remoteDataSource, localDataSource, mapper)
    }

    @Test
    fun getTokenFromCache_HappyCase() {
        runBlocking {
            whenever(localDataSource.loadTokenFromCache()) doReturn tokenEntity
            accountRepository.getTokenFromCache()

            verify(localDataSource).loadTokenFromCache()
            verify(mapper).fromAccessTokenEntity(tokenEntity)
        }
    }

    @Test
    fun refreshToken_HappyCase() {
        runBlocking {
            whenever(remoteDataSource.refreshAccessToken()) doReturn tokenResponse
            whenever(mapper.fromAccessTokenResponse(tokenResponse)) doReturn tokenItem
            whenever(mapper.toAccessTokenEntity(tokenItem)) doReturn tokenEntity

            accountRepository.refreshToken()

            verify(localDataSource).removeTokenFromCache()
            verify(mapper).fromAccessTokenResponse(tokenResponse)
            verify(mapper).toAccessTokenEntity(tokenItem)
            verify(localDataSource).saveTokenToCache(tokenEntity)
        }
    }
}