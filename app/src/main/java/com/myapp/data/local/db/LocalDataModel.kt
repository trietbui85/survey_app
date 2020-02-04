package com.myapp.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class SurveyEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "cover_image_url") val coverImageUrl: String = ""
)

// Response Data Model
data class AccessTokenEntity(
    val accessToken: String = "",
    val tokenType: String = "",
    val expiresIn: Int = 0,
    val createdAt: Int = 0
)