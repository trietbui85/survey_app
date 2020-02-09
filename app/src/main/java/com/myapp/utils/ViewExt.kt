package com.myapp.utils

import android.view.View

// Change visibility of View to VISIBLE
fun View.toVisible() {
  visibility = View.VISIBLE
}

// Change visibility of View to INVISIBLE
fun View.toInvisible() {
  visibility = View.INVISIBLE
}

// Change visibility of View to GONE
fun View.toGone() {
  visibility = View.GONE
}