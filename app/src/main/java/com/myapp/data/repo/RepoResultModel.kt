package com.myapp.data.repo

/**
 * A generic class that holds a value with its loading status.
 *
 * Result is usually created by the Repository classes where they return
 * `LiveData<Result<T>>` to pass back the latest data to the UI with its fetch status.
 */

data class Result<out T>(
    val status: Status,
    val data: T?,
    val exception: DataException?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: DataException): Result<T> {
            return Result(Status.ERROR, null, exception)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null)
        }
    }
}

// This class is used to handle exception when parse data from network
data class DataException(
    val httpCode: Int,
    val errorMessage: String
) :
    Exception(errorMessage) {
    companion object {
        const val ERROR_400_BAD_REQUEST = 400
        const val ERROR_401_UNAUTHORIZED = 401
        const val ERROR_403_FORBIDDEN = 403
        const val ERROR_404_FILE_NOT_FOUND = 404
        const val ERROR_TIMEOUT = 9900
        const val ERROR_JSON_SYNTAX = 9901
        const val ERROR_CONNECTION_EXCEPTION = 9902
        const val ERROR_UNKNOWN_HOST_EXCEPTION = 9903
        const val ERROR_UNKNOWN = 9999
    }
}
