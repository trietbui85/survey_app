package com.myapp.utils

import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

private const val httpHeaderAuthorization = "Authorization"

fun Response.isUnauthorized(): Boolean {
  return this.code == HttpURLConnection.HTTP_UNAUTHORIZED
}

fun Request.getAuthorization(): String? {
  return this.header(httpHeaderAuthorization)
}

fun Request.Builder.addBearerToken(bearerToken: String): Request.Builder {
  return this.removeHeader(httpHeaderAuthorization)
    .header(httpHeaderAuthorization, bearerToken)
}