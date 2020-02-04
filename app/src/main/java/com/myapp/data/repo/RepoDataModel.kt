package com.myapp.data.repo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurveyItem(
    val id: String = "",
    val title: String? = "",
    val description: String = "",
    val coverImageUrl: String = ""
) : Parcelable
