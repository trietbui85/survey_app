package com.myapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.myapp.R
import com.myapp.data.repo.Result
import com.myapp.data.repo.SurveyItem
import com.myapp.ui.detail.DetailFragment
import com.utils.getErrorText
import com.utils.showToastLong
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private lateinit var surveyAdapter: SurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate(): viewModel.fetchSurveys(1)")
        viewModel.fetchSurveys(1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewAndAction()
        initLiveData()
    }

    private fun initLiveData() {
        viewModel.surveyLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    Timber.d("surveyLiveData: is loading")
                    if (viewModel.isFirstTimeLoading()) {
                        viewLoadMore.visibility = View.INVISIBLE
                        viewLoadingFullScreen.visibility = View.VISIBLE
                    } else {
                        viewLoadMore.visibility = View.VISIBLE
                        viewLoadingFullScreen.visibility = View.INVISIBLE
                    }
                }
                Result.Status.ERROR -> {
                    Timber.d("surveyLiveData: error: $it")
                    viewLoadMore.visibility = View.INVISIBLE
                    viewLoadingFullScreen.visibility = View.INVISIBLE

                    val errorText = it.exception.getErrorText(requireContext())
                    this.showToastLong(errorText)
                }
                Result.Status.SUCCESS -> {
                    Timber.d("surveyLiveData: success: ${it.data}")
                    viewLoadMore.visibility = View.INVISIBLE
                    viewLoadingFullScreen.visibility = View.INVISIBLE
                    if (it.data.isNullOrEmpty()) {
                        this.showToastLong(R.string.no_surveys)
                    } else {
                        // TODO Need to restore previous selected position for indicator
                        surveyAdapter.setItems(it.data.orEmpty())

                        indicator.setViewPager(viewPager)
                    }
                }
            }
        })
    }

    private fun initViewAndAction() {
        ivRefresh.setOnClickListener {
            surveyAdapter.clearItems()
            viewModel.fetchSurveys(1, true)
        }
        ivMenu.setOnClickListener {
            // No action for Menu More, thus just show a Toast
            this.showToastLong(R.string.menu_more)
        }

        surveyAdapter = SurveyAdapter(object : OpenDetailCallback {
            override fun click(item: SurveyItem) {
                val bundle = bundleOf(DetailFragment.EXTRA_SURVEY_ITEM to item)
                findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            }
        })

        surveyAdapter.setItems(emptyList())

        viewPager.adapter = surveyAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Timber.d("page change = ${position}/${surveyAdapter.itemCount}")
                if (position == surveyAdapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    interface OpenDetailCallback {
        fun click(item: SurveyItem)
    }
}
