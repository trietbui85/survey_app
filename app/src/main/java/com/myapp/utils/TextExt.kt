package com.myapp.utils

import com.myapp.data.repo.AccessTokenItem
import com.myapp.data.repo.SurveyItem

// Get the Bearer Token from an AccessTokenItem
fun AccessTokenItem.toBearerToken() = "Bearer ${this.accessToken}"

// Get the high resolution version of image path
fun SurveyItem.getHighResImageUrl() = if (coverImageUrl.isEmpty()) "" else coverImageUrl + "l"