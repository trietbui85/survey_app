@file:Suppress("unused")

package com.myapp.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
  msg: String,
  duration: Int = Snackbar.LENGTH_SHORT
) {
  Snackbar.make(this, msg, duration)
      .show()
}

fun View.showSnackBar(@StringRes msgId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
  Snackbar.make(this, msgId, duration)
      .show()
}

fun View.showSnackBarLong(msg: String) {
  Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
      .show()
}

fun View.showSnackBarLong(@StringRes msgId: Int) {
  Snackbar.make(this, msgId, Snackbar.LENGTH_LONG)
      .show()
}