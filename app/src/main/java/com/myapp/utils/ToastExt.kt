package com.myapp.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.showToast(
  msg: String,
  duration: Int = Toast.LENGTH_SHORT
) {
  Toast.makeText(this.requireContext(), msg, duration)
      .show()
}

fun Fragment.showToast(@StringRes msgId: Int, duration: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this.requireContext(), msgId, duration)
      .show()
}

fun Fragment.showToastLong(msg: String) {
  Toast.makeText(this.requireContext(), msg, Toast.LENGTH_LONG)
      .show()
}

fun Fragment.showToastLong(@StringRes msgId: Int) {
  Toast.makeText(this.requireContext(), msgId, Toast.LENGTH_LONG)
      .show()
}
