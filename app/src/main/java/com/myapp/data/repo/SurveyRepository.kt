package com.myapp.data.repo

import com.myapp.data.remote.SurveyRemoteDataSource
import com.myapp.data.remote.model.SurveyResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

interface SurveyRepository {
  fun loadSurveys(
    pageNumber: Int,
    itemPerPage: Int
  ): Flow<Result<List<SurveyItem>>>
}

@ExperimentalCoroutinesApi
class SurveyRepositoryImpl @Inject constructor(
  private val surveyRemoteDataSource: SurveyRemoteDataSource,
  private val mapper: SurveyItemMapper,
  @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
) : SurveyRepository {
  override fun loadSurveys(
    pageNumber: Int,
    itemPerPage: Int
  ): Flow<Result<List<SurveyItem>>> {
    return flow {
      val response: Response<List<SurveyResponse>> =
        surveyRemoteDataSource.getSurveys(pageNumber, itemPerPage)
      if (response.isSuccessful) {
        val items: List<SurveyItem> =
          response.body()!!.map { mapper.fromSurveyResponse(it) }
        emit(Result.success(items))
      } else {
        val exception = response.parseDataException()
          .also {
            Timber.e("loadSurveys: data error: $it")
          }
        emit(Result.error(exception))
      }
    }.catch { e ->
      e.parseConnectionException()
        .also {
          Timber.e("loadSurveys: data error: $it")
          emit(Result.error(it))
        }
    }.flowOn(ioDispatcher)
  }

}
/*
    return try {
      val response: Response<List<SurveyResponse>> =
        surveyRemoteDataSource.getSurveys(pageNumber, itemPerPage)
      if (response.isSuccessful) {
        val items: List<SurveyItem> =
          response.body()!!.map { mapper.fromSurveyResponse(it) }
        Result.success(items)
      } else {
        val exception = response.parseDataException()
          .also {
            Timber.e("loadSurveys: data error: $it")
          }
        Result.error(exception)
      }
    } catch (e: Exception) {
      val exception = e.parseConnectionException()
        .also {
          Timber.e("loadSurveys: connection error: $it")
        }
      Result.error(exception)
    }
*/
