package com.myapp.data.remote.model

// Only list a few fields which are used in UI
data class SurveyResponse(
  val id: String = "",
  val title: String = "",
  val description: String = "",
  val coverImageUrl: String = "",
  val coverBackgroundColor: String = "",
  val type: String = "",
  val surveyVersion: Int = 0,
  val shortUrl: String = ""
)