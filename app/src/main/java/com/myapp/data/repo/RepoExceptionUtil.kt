package com.myapp.data.repo

import retrofit2.Response

// Parse the Response to get correct DataException
fun <T> Response<T>.parseDataException(): DataException {
    val strErrorBody = this.errorBody()?.string() ?: "No response body"

    val httpCode: Int = this.code()

    return DataException(httpCode, strErrorBody)
}
