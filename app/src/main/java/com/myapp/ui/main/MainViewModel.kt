package com.myapp.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.repo.DataException
import com.myapp.data.repo.Result
import com.myapp.data.repo.SurveyItem
import com.myapp.data.repo.SurveyRepository
import com.myapp.utils.CollectionUtils.merge2List
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

  // Current page to load network data, is 0-based
  private var _currentPage = START_PAGE_NUMBER

  // By default, use fullscreen loading in the first time
  private val _loadingFullscreenLiveData = MutableLiveData<Boolean>()
  private val _loadingMoreLiveData = MutableLiveData<Boolean>()
  private val _errorLiveEvent = MutableLiveData<LiveEvent<DataException>>()
  private val _contentLiveData = MutableLiveData<MutableList<SurveyItem>>()
  // -1 mean invalid index, because the ViewPager will have the 0-based index
  private val _indicatorIndexLiveData = MutableLiveData(-1)

  val indicatorIndexLiveData: LiveData<Int>
    get() = _indicatorIndexLiveData

  val contentLiveData: LiveData<MutableList<SurveyItem>>
    get() = _contentLiveData

  val loadingFullscreenLiveData: LiveData<Boolean>
    get() = _loadingFullscreenLiveData

  val loadingMoreLiveData: LiveData<Boolean>
    get() = _loadingMoreLiveData

  val errorLiveEvent: LiveData<LiveEvent<DataException>>
    get() = _errorLiveEvent

  @VisibleForTesting
  fun getCurrentPage() = _currentPage

  fun fetchSurveys(
    pageNumber: Int = START_PAGE_NUMBER,
    forceReload: Boolean = false
  ) {
    Timber.d("Start fetching survey of page $pageNumber")

    fun fetchSurveysForPage(pageNumber: Int) {
      _currentPage = pageNumber
      val showFullscreenLoading = _currentPage == 1

      viewModelScope.launch(mainDispatcher) {
        // Only notify change of fullscreenLoading if showFullscreenLoading is true
        if (showFullscreenLoading) {
          _loadingFullscreenLiveData.value = true
        } else {
          _loadingMoreLiveData.value = true
        }
        val result: Result<List<SurveyItem>> =
          surveyRepository.loadSurveys(_currentPage, numOfItemPerPage)
        if (result.status == Result.Status.SUCCESS) {
          Timber.d(
              "There are ${_contentLiveData.value?.size} existing items, " +
                  "and ${result.data?.size} new items"
          )
          val allItems = merge2List(_contentLiveData.value, result.data)
          (_contentLiveData.value ?: mutableListOf()).toMutableList()
              .also {
                it.addAll(result.data?.toMutableList().orEmpty())
              }
          _contentLiveData.value = allItems
        } else if (result.status == Result.Status.ERROR) {
          _errorLiveEvent.value = LiveEvent(result.exception!!)
          // error means fetching is not successful, thus we must revert currentPage
          _currentPage--
        }

        if (showFullscreenLoading) {
          _loadingFullscreenLiveData.value = false
        } else {
          _loadingMoreLiveData.value = false
        }
      }
    }

    when {
      forceReload || pageNumber == START_PAGE_NUMBER -> {
        // If force reload, will clear the list content
        _currentPage = START_PAGE_NUMBER
        _contentLiveData.value = mutableListOf()
        _indicatorIndexLiveData.value = -1

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

  // Loading next page. Return TRUE if can continue loading, otherwise FALSE if loading is
  // in progress already and we must wait for
  fun loadNextPage(): Boolean {
    if (_loadingFullscreenLiveData.value == true || _loadingMoreLiveData.value == true) {
      Timber.d("Is still fetching data for page $_currentPage, so don't loadNext()")
      return false
    }
    val nextPage = _currentPage + 1
    Timber.d("NextPage fetching survey to page $nextPage")
    fetchSurveys(nextPage)
    return true
  }

  fun setViewPagerSelectedIndex(pageIndex: Int) {
    _indicatorIndexLiveData.value = pageIndex
  }

  private companion object {
    const val START_PAGE_NUMBER = 0
  }
}