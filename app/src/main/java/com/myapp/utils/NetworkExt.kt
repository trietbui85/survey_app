package com.myapp.utils

import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

private const val HTTP_HEADER_AUTHORIZATION = "Authorization"

fun Response.isUnauthorized(): Boolean {
  return this.code == HttpURLConnection.HTTP_UNAUTHORIZED
}

fun Request.getAuthorization(): String? {
  return this.header(HTTP_HEADER_AUTHORIZATION)
}

fun Request.Builder.addBearerToken(bearerToken: String): Request.Builder {
  return this.removeHeader(HTTP_HEADER_AUTHORIZATION)
    .header(HTTP_HEADER_AUTHORIZATION, bearerToken)
}