package com.myapp.data.remote

import com.myapp.data.remote.model.SurveyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SurveyApiService {

  @GET("surveys.json")
  suspend fun getSurveys(
    @Query("page") page: Int = 1,
    @Query("per_page") itemPerPage: Int
  ): Response<List<SurveyResponse>>

}