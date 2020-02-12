package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.remote.SurveyRemoteDataSource
import com.myapp.utils.CoroutinesTest
import com.myapp.utils.TestData.testSurveyItem
import com.myapp.utils.TestData.testSurveyItem2
import com.myapp.utils.TestData.testSurveyResponse
import com.myapp.utils.TestData.testSurveyResponse2
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SurveyRepositoryImplTest : CoroutinesTest() {

  private val remoteDataSource: SurveyRemoteDataSource = mock()
  private val mapper: SurveyItemMapper = mock()

  private lateinit var surveyRepository: SurveyRepositoryImpl

  private val dispatcher = Dispatchers.Unconfined

  @Before
  fun setUp() {
    surveyRepository = SurveyRepositoryImpl(remoteDataSource, mapper, dispatcher)
  }

  /*@Test
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
  }*/

  @Test
  fun loadSurveys_RemoteDataSuccess_ThenEmitNetworkData() = runCoroutineTest {
    val listResponses = listOf(testSurveyResponse, testSurveyResponse2)
    whenever(remoteDataSource.getSurveys(any(), any())) doReturn Response.success(
      200,
      listResponses
    )
    whenever(mapper.fromSurveyResponse(testSurveyResponse)) doReturn testSurveyItem
    whenever(mapper.fromSurveyResponse(testSurveyResponse2)) doReturn testSurveyItem2

    val flow = surveyRepository.loadSurveys(1, 2)

    flow.collect {
      assertThat(it.status).isEqualTo(Result.Status.SUCCESS)
      assertThat(it.data).isEqualTo(listOf(testSurveyItem, testSurveyItem2))
    }
  }
}