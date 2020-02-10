package com.myapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.myapp.R
import com.myapp.data.repo.SurveyItem
import com.myapp.utils.getErrorText
import com.myapp.utils.showToastLong
import com.myapp.utils.toInvisible
import com.myapp.utils.toVisible
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.fullscreenLoadingView
import kotlinx.android.synthetic.main.main_fragment.indicator
import kotlinx.android.synthetic.main.main_fragment.loadMoreView
import kotlinx.android.synthetic.main.main_fragment.menuButton
import kotlinx.android.synthetic.main.main_fragment.refreshButton
import kotlinx.android.synthetic.main.main_fragment.viewPager
import timber.log.Timber
import javax.inject.Inject

class MainFragment : DaggerFragment() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by viewModels<MainViewModel> { viewModelFactory }

  private lateinit var surveyAdapter: SurveyAdapter

  private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
      super.onPageSelected(position)
      Timber.d("page change = ${position}/${surveyAdapter.itemCount}")
      viewModel.setViewPagerSelectedIndex(position)
      if (position == surveyAdapter.itemCount - 1) {
        viewModel.loadNextPage()
      }
    }
  }
  init {
    lifecycleScope.launchWhenCreated {
      val indicatorIndex = viewModel.indicatorIndexLiveData.value ?: -1
      Timber.d("launchWhenCreated: last indicatorIndexLiveData=$indicatorIndex")
      if (indicatorIndex < 0) {
        // If no existing LiveData for content, let fetch data
        viewModel.fetchSurveys(1)
      }

    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onDestroyView() {
    viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    viewPager.adapter = null
    super.onDestroyView()
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    initViewAndAction()
    initLiveData()
  }

  private fun initLiveData() {

    viewModel.contentLiveData.observe(viewLifecycleOwner, Observer {
      Timber.d("viewModel.contentLiveData: success: $it")
      loadMoreView.toInvisible()
      fullscreenLoadingView.toInvisible()
      surveyAdapter.setItems(it!!)

      val indicatorIndex = viewModel.indicatorIndexLiveData.value ?: -1
      if (indicatorIndex >= 0) {
        Timber.d("Restore ViewPager index to $indicatorIndex")
        viewPager.setCurrentItem(indicatorIndex, false)
      }

      indicator.setViewPager(viewPager)
    })

    viewModel.loadingFullscreenLiveData.observe(viewLifecycleOwner, Observer {
      Timber.d("viewModel.loadingFullscreenLiveData: is loading: $it")
      if (it) {
        fullscreenLoadingView.toVisible()
      } else {
        fullscreenLoadingView.toInvisible()
      }
    })

    viewModel.loadingMoreLiveData.observe(viewLifecycleOwner, Observer {
      Timber.d("viewModel.loadingFullscreenLiveData: is loading: $it")
      if (it) {
        loadMoreView.toVisible()
      } else {
        loadMoreView.toInvisible()
      }
    })

    viewModel.errorLiveEvent.observe(viewLifecycleOwner, Observer {
      it.getContentIfNotHandled()
        ?.let { dataException ->
          Timber.d("viewModel.errorLiveEvent: error: $it")
          loadMoreView.toInvisible()
          fullscreenLoadingView.toInvisible()

          val errorText = dataException.getErrorText(requireContext())
          this.showToastLong(errorText)
        }
    })
  }

  private fun initViewAndAction() {
    refreshButton.setOnClickListener {
      surveyAdapter.clearItems()
      viewModel.fetchSurveys(1, true)
    }
    menuButton.setOnClickListener {
      // No action for Menu More, thus just show a Toast
      this.showToastLong(R.string.menu_more)
    }

    surveyAdapter = SurveyAdapter(object : OpenDetailCallback {
      override fun click(item: SurveyItem) {
        findNavController().navigate(MainFragmentDirections.actionToDetailFragment(item))
      }
    })

    viewPager.adapter = surveyAdapter
    viewPager.registerOnPageChangeCallback(pageChangeCallback)

  }

  interface OpenDetailCallback {
    fun click(item: SurveyItem)
  }
}
