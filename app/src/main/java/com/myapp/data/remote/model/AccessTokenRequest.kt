package com.myapp.data.remote.model

import com.google.gson.annotations.SerializedName

// Request Data Model
data class AccessTokenRequest(
  @SerializedName("grant_type") val grantType: String,
  @SerializedName("username") val username: String,
  @SerializedName("password") val password: String
)