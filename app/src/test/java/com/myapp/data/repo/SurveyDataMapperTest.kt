package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.SurveyEntity
import org.junit.Test

class SurveyDataMapperTest {
  private val mapper = SurveyDataMapper()

  @Test
  fun fromSurveyItemToEntity_EmptyItem_ReturnSuccess() {
    val entity: SurveyEntity = mapper.toSurveyEntity(SurveyItem())
    assertThat(entity).isNotNull()
    assertThat(entity.id).isEmpty()
    assertThat(entity.title).isEmpty()
    assertThat(entity.description).isEmpty()
    assertThat(entity.coverImageUrl).isEmpty()
  }

  // TODO(triet) Need to implement Unit Test for all remaining methods

}