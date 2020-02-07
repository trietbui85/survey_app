package com.myapp.data.remote.model

// Response Data Model
data class AccessTokenResponse(
  val accessToken: String = "",
  val tokenType: String = "",
  val expiresIn: Int = 0,
  val createdAt: Int = 0
)