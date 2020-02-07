package com.myapp.data.repo

import com.myapp.data.remote.SurveyRemoteDataSource
import com.myapp.data.remote.model.SurveyResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

interface SurveyRepository {
    suspend fun loadSurveys(pageNumber: Int, itemPerPage: Int): Result<List<SurveyItem>>
}

class SurveyRepositoryImpl @Inject constructor(
    private val surveyRemoteDataSource: SurveyRemoteDataSource,
    private val mapper: SurveyDataMapper
) : SurveyRepository {
    override suspend fun loadSurveys(pageNumber: Int, itemPerPage: Int): Result<List<SurveyItem>> {

        return try {
            val response: Response<List<SurveyResponse>> =
                surveyRemoteDataSource.getSurveys(pageNumber, itemPerPage)
            if (response.isSuccessful) {
                val items: List<SurveyItem> =
                    response.body()!!.map { mapper.fromSurveyResponse(it) }
                Result.success(items)
            } else {
                val exception = response.parseDataException().also {
                    Timber.e("loadSurveys: data error: $it")
                }
                Result.error(exception)
            }
        } catch (e: Exception) {
            val exception = e.parseConnectionException().also {
                Timber.e("loadSurveys: connection error: $it")
            }
            Result.error(exception)
        }


    }


}