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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
  private val surveyRepository: SurveyRepository,
  @Named("NumOfItemPerPage") private val numOfItemPerPage: Int
) : ViewModel() {

  // Current page to load network data, is 0-based
  private var currentPage = START_PAGE_NUMBER
  private var endingPage = -1

  // By default, use fullscreen loading in the first time
  private val _loadingFullscreenLiveData = MutableLiveData<Boolean>()
  private val _loadingMoreLiveData = MutableLiveData<Boolean>()
  private val _errorLiveEvent = MutableLiveData<LiveEvent<DataException>>()
  private val _contentLiveData = MutableLiveData<MutableList<SurveyItem>>()
  // -1 means invalid index, because the ViewPager will have the 0-based index
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
  fun getCurrentPage() = currentPage

  // After the Fragment is recreated, it will refetch the surveys
  // Thus we need to check if surveys is already existed, then no need to refetch. Otherwise
  // will start fetching
  fun fetchSurveysIfPossible() {
    val indicatorIndex = indicatorIndexLiveData.value ?: -1
    // Only in the first time we have no page yet, thus need to refetch
    if (indicatorIndex < 0) {
      fetchSurveys()
    }
  }

  fun fetchSurveys(
    pageNumber: Int = START_PAGE_NUMBER,
    forceReload: Boolean = false
  ) {
    Timber.d("Start fetching survey of page $pageNumber")

    fun fetchSurveysForPage(pageNumber: Int) {
      currentPage = pageNumber
      Timber.d("Before fetchSurveysForPage, set currentPage = $pageNumber")
      val showFullscreenLoading = currentPage == START_PAGE_NUMBER

      // Only notify change of fullscreenLoading if showFullscreenLoading is true
      surveyRepository.loadSurveys(currentPage, numOfItemPerPage)
        .onStart {
          if (showFullscreenLoading) {
            _loadingFullscreenLiveData.value = true
          } else {
            _loadingMoreLiveData.value = true
          }
        }.onCompletion {
          if (showFullscreenLoading) {
            _loadingFullscreenLiveData.value = false
          } else {
            _loadingMoreLiveData.value = false
          }
        }.onEach { result ->
          if (result.status == Result.Status.SUCCESS) {
            Timber.d("There are ${_contentLiveData.value?.size} existing items, and ${result.data?.size} new items")

            if (result.data.isNullOrEmpty()) {
              // If success but has empty list data, it means no more page to load
              // Thus we must revert currentPage
              currentPage--
              endingPage = currentPage
              Timber.d("This is the endingPage=$endingPage. No more items")
            } else {
              merge2List(_contentLiveData.value, result.data).let {
                _contentLiveData.value = it
              }
            }

          } else if (result.status == Result.Status.ERROR) {
            _errorLiveEvent.value = LiveEvent(result.exception!!)
            // error means fetching is not successful, thus we must revert currentPage
            currentPage--
          }
        }.launchIn(viewModelScope)
    }

    when {
      forceReload || pageNumber == START_PAGE_NUMBER -> {
        _contentLiveData.value = mutableListOf()
        _indicatorIndexLiveData.value = -1

        fetchSurveysForPage(START_PAGE_NUMBER)
      }
      pageNumber != currentPage -> {
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
      Timber.d("Is still fetching data for page $currentPage, so don't loadNext()")
      return false
    }

    if (endingPage >= 0) {
      Timber.d("$endingPage is the ending page. No more page to loadNext()")
      return false
    }

    val nextPage = currentPage + 1
    Timber.d("NextPage fetching survey to page $nextPage")
    fetchSurveys(nextPage)
    return true
  }

  fun setViewPagerSelectedIndex(pageIndex: Int) {
    _indicatorIndexLiveData.value = pageIndex
  }

  fun onScrollToPage(pageIndex: Int, total: Int) {
    _indicatorIndexLiveData.value = pageIndex
    if (pageIndex == total - VISIBLE_THRESHOLD) {
      loadNextPage()
    }
  }

  private companion object {
    const val START_PAGE_NUMBER = 0
    // The minimum number of items to have below your current scroll position
    // before loading more
    const val VISIBLE_THRESHOLD = 1
  }
}