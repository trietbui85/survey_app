package com.myapp.data.repo

import com.myapp.data.local.db.AccessTokenEntity
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
}

class AccountItemMapper {
  fun fromAccessTokenResponse(response: AccessTokenResponse) = AccessTokenItem(response.accessToken)

  fun fromAccessTokenEntity(entity: AccessTokenEntity) = AccessTokenItem(entity.accessToken)

  fun toAccessTokenEntity(item: AccessTokenItem) =
    AccessTokenEntity(item.accessToken)

  // Convert an AccessTokenEntity to JSON text
  fun fromAccessTokenEntityToString(entity: AccessTokenEntity): String = entity.accessToken

  // Convert JSON text to an instance of AccessTokenEntity, or null
  fun fromStringToAccessTokenEntity(token: String?): AccessTokenEntity? {
    if (token.isNullOrBlank()) {
      return null
    }
    return AccessTokenEntity(token)
  }

}