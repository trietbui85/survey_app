package com.myapp.data.remote.model

import com.google.gson.annotations.SerializedName

// Only list a few fields which are used in UI
data class SurveyResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("cover_image_url") val coverImageUrl: String,
    @SerializedName("cover_background_color") val coverBackgroundColor: String,
    @SerializedName("type") val type: String,
    @SerializedName("survey_version") val surveyVersion: Int = 0,
    @SerializedName("short_url") val shortUrl: String
)