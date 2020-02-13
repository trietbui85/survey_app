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
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class SurveyRepositoryImplTest : CoroutinesTest() {

  private val remoteDataSource: SurveyRemoteDataSource = mock()
  private val mapper: SurveyItemMapper = mock()

  private lateinit var surveyRepository: SurveyRepositoryImpl

  private val dispatcher = Dispatchers.Unconfined

  @Before
  fun setUp() {
    surveyRepository = SurveyRepositoryImpl(remoteDataSource, mapper, dispatcher)
  }

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
      assertThat(it.exception).isNull()
    }
  }

  @Test
  fun loadSurveys_RemoteError400_ThenEmitException() = runCoroutineTest {
    // Thanks https://stackoverflow.com/a/33156790/190309 for sample code to
    // create Response.error()
    whenever(remoteDataSource.getSurveys(any(), any())) doReturn Response.error(
      400,
      "Bad request"
        .toResponseBody("application/json".toMediaTypeOrNull())
    )

    val flow = surveyRepository.loadSurveys(1, 2)

    flow.collect {
      assertThat(it.status).isEqualTo(Result.Status.ERROR)
      assertThat(it.data).isNull()
      assertThat(it.exception).isEqualTo(DataException(400, "Bad request"))
    }
  }
}