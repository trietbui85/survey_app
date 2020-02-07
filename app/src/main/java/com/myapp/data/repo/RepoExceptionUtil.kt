package com.myapp.data.repo

import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

// Parse the Response to get correct DataException
fun <T> Response<T>.parseDataException(): DataException {
  val strErrorBody = this.errorBody()?.string() ?: "No response body"

  val httpCode: Int = this.code()

  return DataException(httpCode, strErrorBody)
}

// Parse the exception to get correct DataException
fun Throwable.parseConnectionException(): DataException {
  when (this) {
    is DataException -> return this
    is HttpException -> {
      return DataException(
          httpCode = this.code(),
          errorMessage = this.message()
      )
    }
    is UnknownHostException -> {
      return DataException(
          httpCode = DataException.ERROR_UNKNOWN_HOST_EXCEPTION,
          errorMessage = this.message ?: "Failed to connect to server"
      )
    }
    is ConnectException -> {
      return DataException(
          httpCode = DataException.ERROR_CONNECTION_EXCEPTION,
          errorMessage = this.message ?: "Failed to connect to server"
      )
    }
    is SocketTimeoutException -> {
      return DataException(
          httpCode = DataException.ERROR_TIMEOUT,
          errorMessage = this.cause?.message ?: "Socket timeout Exception"
      )
    }
    is JsonSyntaxException -> {
      return DataException(
          httpCode = DataException.ERROR_JSON_SYNTAX,
          errorMessage = this.cause?.message ?: "JsonSyntaxException"
      )
    }
    else -> {
      return DataException(
          httpCode = DataException.ERROR_UNKNOWN,
          errorMessage = this.message ?: this.cause?.message ?: "Unknown Exception"
      )
    }
  }
}