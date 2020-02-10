package com.myapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.myapp.data.repo.DataException
import com.myapp.data.repo.SurveyItem
import com.myapp.data.repo.SurveyRepository
import com.myapp.utils.LiveEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

  // Run tasks synchronously
  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private val surveyRepository: SurveyRepository = mockk(relaxed = true)

  private lateinit var viewModel: MainViewModel

  private val dispatcher = Dispatchers.Unconfined

  private val surveys = mutableListOf(SurveyItem("id 1"), SurveyItem("id 2"))
  private val dataException = DataException(404, "Page not found")
  private val resultSuccess = com.myapp.data.repo.Result.success(surveys)
  private val resultError = com.myapp.data.repo.Result.error<List<SurveyItem>>(dataException)

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    viewModel = MainViewModel(surveyRepository, 5, dispatcher)
  }

  @Test
  fun fetchSurveys_FirstPageAndSuccess_ThenWithTheseResults() = runBlocking {
    val pageNumber = 0
    coEvery { surveyRepository.loadSurveys(pageNumber, any()) } returns resultSuccess

    val loadingFullscreenObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingFullscreenLiveData.observeForever(loadingFullscreenObserver)

    val loadingMoreObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingMoreLiveData.observeForever(loadingMoreObserver)

    val contentObserver = mockk<Observer<MutableList<SurveyItem>>>(relaxed = true)
    viewModel.contentLiveData.observeForever(contentObserver)

    // TODO(triet) Must write test for _indicatorIndexLiveData too

    // Invoke method `fetchSurveys()`
    viewModel.fetchSurveys(pageNumber)

    verifySequence {
      // reset data when load first page
      contentObserver.onChanged(mutableListOf())
      // is loading, will fullscreen
      loadingFullscreenObserver.onChanged(true)
      // receive success data
      contentObserver.onChanged(resultSuccess.data)
      // stop loading, will fullscreen
      loadingFullscreenObserver.onChanged(false)
    }

    assertThat(viewModel.getCurrentPage()).isEqualTo(pageNumber)
  }

  @Test
  fun fetchSurveys_FirstPageAndFailed_ThenWithTheseResults() = runBlocking {
    val pageNumber = 0
    coEvery { surveyRepository.loadSurveys(pageNumber, any()) } returns resultError

    val loadingFullscreenObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingFullscreenLiveData.observeForever(loadingFullscreenObserver)

    val loadingMoreObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingMoreLiveData.observeForever(loadingMoreObserver)

    val contentObserver = mockk<Observer<MutableList<SurveyItem>>>(relaxed = true)
    viewModel.contentLiveData.observeForever(contentObserver)

    val errorObserver = mockk<Observer<LiveEvent<DataException>>>(relaxed = true)
    viewModel.errorLiveEvent.observeForever(errorObserver)

    viewModel.fetchSurveys(pageNumber)

    verifySequence {
      // reset data when load first page
      contentObserver.onChanged(mutableListOf())
      // is loading, will fullscreen
      loadingFullscreenObserver.onChanged(true)
      // receive error
      errorObserver.onChanged(LiveEvent(resultError.exception!!))
      // stop loading, will fullscreen
      loadingFullscreenObserver.onChanged(false)
    }
    assertThat(viewModel.getCurrentPage()).isLessThan(pageNumber)
  }

  @Test
  fun fetchSurveys_SecondPageAndSuccess_ThenWithTheseResults() = runBlocking {
    val pageNumber = 1
    coEvery { surveyRepository.loadSurveys(pageNumber, any()) } returns resultSuccess

    val loadingFullscreenObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingFullscreenLiveData.observeForever(loadingFullscreenObserver)

    val loadingMoreObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingMoreLiveData.observeForever(loadingMoreObserver)

    val contentObserver = mockk<Observer<MutableList<SurveyItem>>>(relaxed = true)
    viewModel.contentLiveData.observeForever(contentObserver)

    viewModel.fetchSurveys(pageNumber)

    verifySequence {
      // there will be NO reset data when load second page
      // first is loading, will fullscreen
      loadingMoreObserver.onChanged(true)
      // receive success data
      contentObserver.onChanged(resultSuccess.data)
      // stop loading, will fullscreen
      loadingMoreObserver.onChanged(false)
    }
    assertThat(viewModel.getCurrentPage()).isEqualTo(pageNumber)
  }

  @Test
  fun fetchSurveys_SecondPageAndFailed_ThenWithTheseResults() = runBlocking {
    val pageNumber = 1
    coEvery { surveyRepository.loadSurveys(pageNumber, any()) } returns resultError

    val loadingFullscreenObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingFullscreenLiveData.observeForever(loadingFullscreenObserver)

    val loadingMoreObserver = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.loadingMoreLiveData.observeForever(loadingMoreObserver)

    val errorObserver = mockk<Observer<LiveEvent<DataException>>>(relaxed = true)
    viewModel.errorLiveEvent.observeForever(errorObserver)

    viewModel.fetchSurveys(pageNumber)

    verifySequence {
      // there will be NO reset data when load second page
      // is loading, will fullscreen
      loadingMoreObserver.onChanged(true)
      // receive error
      errorObserver.onChanged(LiveEvent(resultError.exception!!))
      // stop loading, will fullscreen
      loadingMoreObserver.onChanged(false)
    }
    // Make sure if failed, then currentPage is previous page
    assertThat(viewModel.getCurrentPage()).isEqualTo(pageNumber - 1)
  }

}