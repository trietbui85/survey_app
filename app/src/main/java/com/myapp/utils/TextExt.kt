package com.myapp.utils

import com.myapp.data.repo.AccessTokenItem

// Get the Bearer Token from an AccessTokenItem
fun AccessTokenItem.toBearerToken() = "Bearer ${this.accessToken}"
