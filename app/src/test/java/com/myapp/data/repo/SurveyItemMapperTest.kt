package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.remote.model.SurveyResponse
import com.myapp.utils.TestData.testSurveyItem
import com.myapp.utils.TestData.testSurveyResponse
import org.junit.Test

class SurveyItemMapperTest {
  private val mapper = SurveyItemMapper()

  @Test
  fun fromSurveyResponseToItem_EmptyResponse_ReturnSuccess() {
    val item: SurveyItem = mapper.fromSurveyResponse(SurveyResponse())
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(SurveyItem())
  }

  @Test
  fun fromSurveyResponseToItem_ValidResponse_ReturnSuccess() {
    val item: SurveyItem = mapper.fromSurveyResponse(testSurveyResponse)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(testSurveyItem)
  }

}