package com.myapp.utils

import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.local.db.SurveyEntity
import com.myapp.data.remote.model.AccessTokenResponse
import com.myapp.data.repo.AccessTokenItem

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

  private val surveyEntity1 = createSurveyEntity(1)
  private val surveyEntity2 = createSurveyEntity(2)
  private val surveyEntity3 = createSurveyEntity(3)
  val testSurveyEntities = listOf(surveyEntity1, surveyEntity2, surveyEntity3)

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
}