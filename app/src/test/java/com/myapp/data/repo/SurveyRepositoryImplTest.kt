package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.remote.SurveyRemoteDataSource
import com.myapp.data.remote.model.SurveyResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class SurveyRepositoryImplTest {

  private val remoteDataSource: SurveyRemoteDataSource = mock()
  private val mapper: SurveyItemMapper = mock()

  private lateinit var surveyRepository: SurveyRepositoryImpl

  @Before
  fun setUp() {
    surveyRepository = SurveyRepositoryImpl(remoteDataSource, mapper)
  }

  @Test
  fun getTokenFromCache_HappyCase() {
    runBlocking {
      val listSurveys = listOf(SurveyResponse(id = "id 1"), SurveyResponse(id = "id 2"))
      whenever(remoteDataSource.getSurveys(any(), any())) doReturn Response.success(
          200,
          listSurveys
      )

      val response = surveyRepository.loadSurveys(1, 2)

      verify(remoteDataSource).getSurveys(1, 2)
      assertThat(response).isNotNull()
      assertThat(response.status).isEqualTo(Result.Status.SUCCESS)
//            assertThat(response.data).containsExactlyElementsIn(listSurveys)
//            verify(mapper).fromAccessTokenEntity(tokenEntity)
    }
  }
}