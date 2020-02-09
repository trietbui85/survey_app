package com.myapp.data.repo

import com.google.gson.Gson
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.local.db.SurveyEntity
import com.myapp.data.remote.model.AccessTokenResponse
import com.myapp.data.remote.model.SurveyResponse
import javax.inject.Inject

class SurveyItemMapper @Inject constructor() {

  fun fromSurveyResponse(response: SurveyResponse) = SurveyItem(
      id = response.id,
      title = response.title,
      description = response.description,
      coverImageUrl = response.coverImageUrl
  )

  fun fromSurveyEntity(entity: SurveyEntity) = SurveyItem(
      id = entity.id,
      title = entity.title,
      description = entity.description,
      coverImageUrl = entity.coverImageUrl
  )

  fun toSurveyEntity(item: SurveyItem) = SurveyEntity(
      id = item.id,
      title = item.title,
      description = item.description,
      coverImageUrl = item.coverImageUrl
  )
}

class AccountItemMapper @Inject constructor(private val gson: Gson) {
  fun fromAccessTokenResponse(response: AccessTokenResponse) = AccessTokenItem(
      accessToken = response.accessToken,
      tokenType = response.tokenType,
      createdAt = response.createdAt,
      expiresIn = response.expiresIn
  )

  fun fromAccessTokenEntity(entity: AccessTokenEntity) = AccessTokenItem(
      accessToken = entity.accessToken,
      tokenType = entity.tokenType,
      createdAt = entity.createdAt,
      expiresIn = entity.expiresIn
  )

  fun toAccessTokenEntity(item: AccessTokenItem) =
    AccessTokenEntity(
        accessToken = item.accessToken,
        tokenType = item.tokenType,
        createdAt = item.createdAt,
        expiresIn = item.expiresIn
    )

  // Convert an AccessTokenEntity to JSON text
  fun fromAccessTokenEntityToString(entity: AccessTokenEntity): String = gson.toJson(entity)

  // Convert JSON text to an instance of AccessTokenEntity, or null
  fun fromStringToAccessTokenEntity(jsonToken: String?): AccessTokenEntity? {
    if (jsonToken.isNullOrBlank()) {
      return null
    }
    return try {
      gson.fromJson(jsonToken, AccessTokenEntity::class.java)
    } catch (e: Exception) {
      // Log.e("RepoDataMapper", "fromStringToAccessTokenEntity failed: $e")
      null
    }
  }

}