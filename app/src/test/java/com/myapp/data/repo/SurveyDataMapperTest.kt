package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.SurveyEntity
import com.myapp.data.remote.model.SurveyResponse
import com.myapp.utils.TestData.testSurveyEntity
import com.myapp.utils.TestData.testSurveyItem
import com.myapp.utils.TestData.testSurveyResponse
import org.junit.Test

class SurveyDataMapperTest {
  private val mapper = SurveyDataMapper()

  @Test
  fun fromSurveyItemToEntity_EmptyItem_ReturnSuccess() {
    val entity: SurveyEntity = mapper.toSurveyEntity(SurveyItem())
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(SurveyEntity())
  }

  @Test
  fun fromSurveyItemToEntity_ValidItem_ReturnSuccess() {
    val entity: SurveyEntity = mapper.toSurveyEntity(testSurveyItem)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(testSurveyEntity)
  }

  @Test
  fun fromSurveyEntityToItem_EmptyEntity_ReturnSuccess() {
    val item: SurveyItem = mapper.fromSurveyEntity(SurveyEntity())
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(SurveyItem())
  }

  @Test
  fun fromSurveyEntityToItem_ValidEntity_ReturnSuccess() {
    val item: SurveyItem = mapper.fromSurveyEntity(testSurveyEntity)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(testSurveyItem)
  }

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

  // TODO(triet) Need to implement Unit Test for all remaining methods

}