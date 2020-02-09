package com.myapp.utils

import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.local.db.SurveyEntity

object TestData {
  private fun createSurveyEntity(index: Int) = SurveyEntity(
    "id $index", "title $index", "description $index", "image $index"
  )

  private val surveyEntity1 = createSurveyEntity(1)
  private val surveyEntity2 = createSurveyEntity(2)
  private val surveyEntity3 = createSurveyEntity(3)
  val surveyEntities = listOf(surveyEntity1, surveyEntity2, surveyEntity3)

  private const val TOKEN_VALUE = "token_data"
  private const val TOKEN_TYPE = "token_type"
  private const val EXPIRED_IN = 2
  private const val CREATED_AT = 1

  val jsonTokenData = """
            {"access_token":"$TOKEN_VALUE","token_type":"$TOKEN_TYPE","expires_in":$EXPIRED_IN,"created_at":$CREATED_AT}
        """.trimIndent()

  val tokenEntity = AccessTokenEntity(
    accessToken = TOKEN_VALUE,
    tokenType = TOKEN_TYPE,
    expiresIn = EXPIRED_IN,
    createdAt = CREATED_AT
  )
}