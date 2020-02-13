package com.myapp.data.local.db

// Response Data Model
data class AccessTokenEntity(
  val accessToken: String = "",
  val tokenType: String = "",
  val expiresIn: Int = 0,
  val createdAt: Int = 0
)