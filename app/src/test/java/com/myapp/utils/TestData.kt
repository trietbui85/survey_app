package com.myapp.utils

import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.local.db.SurveyEntity
import com.myapp.data.remote.model.AccessTokenResponse
import com.myapp.data.remote.model.SurveyResponse
import com.myapp.data.repo.AccessTokenItem
import com.myapp.data.repo.SurveyItem

object TestData {
  private fun createSurveyEntity(index: Int) = SurveyEntity(
    "id $index", "title $index", "description $index", "image $index"
  )

  private fun buildJsonData(
    token: String,
    tokenType: String,
    expiresIn: Int,
    createdAt: Int
  ): String {
    return """
            {"access_token":"$token","token_type":"$tokenType","expires_in":$expiresIn,"created_at":$createdAt}
        """.trimIndent()
  }

  val testSurveyEntities = listOf(
    createSurveyEntity(1),
    createSurveyEntity(2),
    createSurveyEntity(3)
  )

  private const val TOKEN_VALUE = "token_data"
  private const val TOKEN_TYPE = "token_type"
  private const val EXPIRED_IN = 2
  private const val CREATED_AT = 1

  val testJsonToken = buildJsonData(TOKEN_VALUE, TOKEN_TYPE, EXPIRED_IN, CREATED_AT)
  val testJsonTokenEmpty = buildJsonData("", "", 0, 0)

  val testTokenEntity = AccessTokenEntity(
    accessToken = TOKEN_VALUE,
    tokenType = TOKEN_TYPE,
    expiresIn = EXPIRED_IN,
    createdAt = CREATED_AT
  )
  val testTokenResponse = AccessTokenResponse(
    accessToken = TOKEN_VALUE,
    tokenType = TOKEN_TYPE,
    expiresIn = EXPIRED_IN,
    createdAt = CREATED_AT
  )
  val testTokenItem = AccessTokenItem(
    accessToken = TOKEN_VALUE,
    tokenType = TOKEN_TYPE,
    expiresIn = EXPIRED_IN,
    createdAt = CREATED_AT
  )

  private const val SURVEY_ID = "survey id"
  private const val SURVEY_TITLE = "survey title"
  private const val SURVEY_DESCRIPTION = "survey description"
  private const val SURVEY_COVER_IMAGE_URL = "survey cover image url"

  val testSurveyResponse = SurveyResponse(
    SURVEY_ID, SURVEY_TITLE, SURVEY_DESCRIPTION,
    SURVEY_COVER_IMAGE_URL, coverBackgroundColor = "#00FF00", type = "unknown type",
    shortUrl = "bit.ly", surveyVersion = 1
  )
  val testSurveyEntity =
    SurveyEntity(SURVEY_ID, SURVEY_TITLE, SURVEY_DESCRIPTION, SURVEY_COVER_IMAGE_URL)
  val testSurveyItem =
    SurveyItem(SURVEY_ID, SURVEY_TITLE, SURVEY_DESCRIPTION, SURVEY_COVER_IMAGE_URL)
}