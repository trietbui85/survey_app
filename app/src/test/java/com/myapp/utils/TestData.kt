package com.myapp.utils

import com.myapp.data.local.db.SurveyEntity

object TestData {
  private fun createSurveyEntity(index: Int) = SurveyEntity(
    "id $index", "title $index", "description $index", "image $index"
  )

  val surveyEntity1 = createSurveyEntity(1)
  val surveyEntity2 = createSurveyEntity(2)
  val surveyEntity3 = createSurveyEntity(3)
  val surveyEntities = listOf(surveyEntity1, surveyEntity2, surveyEntity3)
}