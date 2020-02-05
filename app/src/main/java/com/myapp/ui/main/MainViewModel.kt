package com.myapp.ui.main

import androidx.lifecycle.*
import com.myapp.data.repo.Result
import com.myapp.data.repo.ResultSurveys
import com.myapp.data.repo.SurveyRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    @Named("NumOfItemPerPage") private val numOfItemPerPage: Int
) : ViewModel() {

    private val _pageNumberLiveData = MutableLiveData<Int>()

    private val _surveyLiveData = _pageNumberLiveData.switchMap {
        liveData {
            emit(Result.loading(null))
            val data = surveyRepository.loadSurveys(_pageNumberLiveData.value!!, numOfItemPerPage)
            emit(data)
        }
    }

    val surveyLiveData: LiveData<ResultSurveys>
        get() = _surveyLiveData

    fun fetchSurveys(pageNumber: Int, forceReload: Boolean = false) {
        Timber.d("Start fetching survey of page $pageNumber")
        if (pageNumber != _pageNumberLiveData.value || forceReload) {
            _pageNumberLiveData.value = pageNumber
        } else {
            Timber.d("... but still the same page. No need to fetchSurveys")
        }

    }

    fun loadNextPage() {
        val nextPage = _pageNumberLiveData.value?.plus(1) ?: 1
        Timber.d("NextPage fetching survey to page $nextPage")
        fetchSurveys(nextPage)
    }

    fun getCurrentPage() = _pageNumberLiveData.value ?: 1
    fun isFirstTimeLoading() = _pageNumberLiveData.value == 1
}
