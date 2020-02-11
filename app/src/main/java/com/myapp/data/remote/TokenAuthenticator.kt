package com.myapp.data.remote

import com.myapp.data.repo.AccessTokenItem
import com.myapp.data.repo.AccountRepository
import com.myapp.utils.addBearerToken
import com.myapp.utils.getAuthorization
import com.myapp.utils.isUnauthorized
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class TokenAuthenticator(
  private val accountRepository: AccountRepository
) : Authenticator {

  override fun authenticate(
    route: Route?,
    response: Response
  ): Request? {
    Timber.d("Found 401 request - need to refresh its token")

    if (getResponseCount(response) >= retryLimit) {
      Timber.d("Failed $retryLimit times, giving up")
      return null
    }

    // We need to have a token in order to refresh it.
    val token = runBlocking {
      accountRepository.getTokenFromCache()
    }

    synchronized(this) {
      val newToken = runBlocking {
        accountRepository.getTokenFromCache()
      }

      // Check if the request made was previously made as an authenticated request.
      if (response.isUnauthorized() || response.request.getAuthorization() != null) {
        response.priorResponse

        // If the token has changed since the request was made, use the new token.
        if (newToken != null && newToken != token) {
          return newRequestWithAccessToken(response.request, newToken)
        }

        val updatedToken = runBlocking {
          accountRepository.refreshToken()
        } ?: return null

        // Retry the request with the new token.
        return newRequestWithAccessToken(response.request, updatedToken)
      }
    }
    return null
  }

  private fun getResponseCount(response: Response): Int {
    var result = 1
    var priorResponse = response.priorResponse
    while (priorResponse != null) {
      result++
      priorResponse = priorResponse.priorResponse
    }
    return result
  }

  private fun newRequestWithAccessToken(
    request: Request,
    accessToken: AccessTokenItem
  ): Request {
    Timber.d("After have new token=$accessToken, continue request=$request")
    return request.newBuilder()
      .addBearerToken(accessToken.bearerToken)
      .build()
  }

  private companion object {
    // Only retry 3 times
    const val retryLimit = 3
  }
}