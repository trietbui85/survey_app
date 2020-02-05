package com.utils

import android.content.Context
import com.myapp.R
import com.myapp.data.repo.DataException

fun DataException?.getErrorText(context: Context): String {
    if (this == null) return context.getString(R.string.error_unknown)
    return when (httpCode) {
        DataException.ERROR_400_BAD_REQUEST -> context.getString(R.string.error_server)
        DataException.ERROR_401_UNAUTHORIZED -> context.getString(R.string.error_server)
        DataException.ERROR_403_FORBIDDEN -> context.getString(R.string.error_server)
        DataException.ERROR_404_FILE_NOT_FOUND -> context.getString(R.string.error_server)
        DataException.ERROR_TIMEOUT -> context.getString(R.string.error_connection)
        DataException.ERROR_CONNECTION_EXCEPTION -> context.getString(R.string.error_connection)
        DataException.ERROR_UNKNOWN_HOST_EXCEPTION -> context.getString(R.string.error_connection)
        DataException.ERROR_JSON_SYNTAX -> context.getString(R.string.error_parse)
        else -> context.getString(R.string.error_unknown)
    }
}