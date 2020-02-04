package com.myapp.data.remote

import com.myapp.data.remote.model.AccessTokenRequest
import com.myapp.data.remote.model.AccessTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApiService {

    @POST("oauth/token")
    suspend fun refreshAccessToken(@Body tokenRequest: AccessTokenRequest): AccessTokenResponse

}