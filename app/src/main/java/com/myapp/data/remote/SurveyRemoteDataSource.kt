package com.myapp.data.remote

import com.myapp.data.remote.model.SurveyResponse
import retrofit2.Response

interface SurveyRemoteDataSource {

  suspend fun getSurveys(
    page: Int,
    itemPerPage: Int
  ): Response<List<SurveyResponse>>
}

class SurveyRemoteDataSourceImpl(
  private val surveyApiService: SurveyApiService
) : SurveyRemoteDataSource {

  override suspend fun getSurveys(
    page: Int,
    itemPerPage: Int
  ): Response<List<SurveyResponse>> {
    return surveyApiService.getSurveys(page, itemPerPage)
  }
}