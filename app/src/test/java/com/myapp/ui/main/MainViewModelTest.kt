package com.myapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.myapp.data.repo.ResultSurveys
import com.myapp.data.repo.SurveyItem
import com.myapp.data.repo.SurveyRepository
import com.myapp.rule.CoroutineTestRule
import com.myapp.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val surveyRepository: SurveyRepository = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(surveyRepository, 5)
    }

    @Test
    fun fetchSurveys() = runBlockingTest {
        val surveys: List<SurveyItem> = listOf(SurveyItem("id 1"), SurveyItem("id 2"))
        val result: ResultSurveys = com.myapp.data.repo.Result.success(surveys)
        whenever(surveyRepository.loadSurveys(1, 1)) doReturn result

        viewModel.fetchSurveys(1)
        viewModel.surveyLiveData.getOrAwaitValue().let {
            assertThat(it).isNotNull()
            assertThat(it).isEqualTo(com.myapp.data.repo.Result.loading(null))
        }
    }

}