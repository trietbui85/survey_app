package com.myapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.repo.DataException
import com.myapp.data.repo.Result
import com.myapp.data.repo.SurveyItem
import com.myapp.data.repo.SurveyRepository
import com.myapp.utils.LiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository,
    @Named("NumOfItemPerPage") private val numOfItemPerPage: Int,
    @Named("MainDispatcher") private val mainDispatcher: CoroutineDispatcher,
    @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _currentPage = 1

    // private val _pageNumberLiveData = MutableLiveData<Int>()
    // Pair<Boolean, Boolean> mean <IsLoading, IsFirstPage>
    private val _loadingLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    private val _errorLiveEvent = MutableLiveData<LiveEvent<DataException>>()
    private val _contentLiveData = MutableLiveData<List<SurveyItem>>(emptyList())
    // -1 mean invalid index, because the ViewPager will have the 0-based index
    private val _pageIndexLiveData = MutableLiveData(-1)

    val pageIndexLiveData: LiveData<Int>
        get() = _pageIndexLiveData

    val contentLiveData: LiveData<List<SurveyItem>>
        get() = _contentLiveData

    val loadingLiveData: LiveData<Pair<Boolean, Boolean>>
        get() = _loadingLiveData

    val errorLiveEvent: LiveData<LiveEvent<DataException>>
        get() = _errorLiveEvent

    fun fetchSurveys(pageNumber: Int = 1, forceReload: Boolean = false) {
        Timber.d("Start fetching survey of page $pageNumber")

        fun fetchSurveysForPage(pageNumber: Int) {
            _currentPage = pageNumber
            viewModelScope.launch(mainDispatcher) {
                _loadingLiveData.value = Pair(true, _currentPage == 1)
                val result: Result<List<SurveyItem>> =
                    surveyRepository.loadSurveys(_currentPage, numOfItemPerPage)
                if (result.status == Result.Status.SUCCESS) {
                    Timber.d(
                        "There are ${_contentLiveData.value?.size} existing items, " +
                                "and ${result.data?.size} new items"
                    )
                    val allItems = _contentLiveData.value?.toMutableList()?.also {
                        it.addAll(result.data?.toMutableList().orEmpty())
                    }
                    _contentLiveData.value = allItems
                } else if (result.status == Result.Status.ERROR) {
                    _errorLiveEvent.value = LiveEvent(result.exception!!)
                }

                _loadingLiveData.value = Pair(false, _currentPage == 1)
            }
        }

        when {
            forceReload || pageNumber == 1 -> {
                // If force reload, will clear the list content
                _currentPage = 1
                _contentLiveData.value = emptyList()
                _pageIndexLiveData.value = -1

                fetchSurveysForPage(1)
            }
            pageNumber != _currentPage -> {
                fetchSurveysForPage(pageNumber)
            }
            else -> {
                Timber.d("... but still the same page. No need to fetchSurveys")
            }
        }

    }

    fun loadNextPage() {
        val nextPage = _currentPage + 1
        Timber.d("NextPage fetching survey to page $nextPage")
        fetchSurveys(nextPage)
    }

    fun setViewPagerSelectedIndex(pageIndex: Int) {
        _pageIndexLiveData.value = pageIndex
    }
}