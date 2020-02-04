package com.myapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.myapp.R
import com.myapp.data.repo.SurveyItem
import com.myapp.ui.detail.DetailFragment
import com.utils.showToastLong
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var surveyAdapter: SurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initViewAndAction()

        initLiveData()
    }

    private fun initLiveData() {
        // TODO Listen for ViewModel.
    }

    private fun initViewAndAction() {
        ivRefresh.setOnClickListener {
            surveyAdapter.clearItems()
            // TODO Force to re-fetch data
            this.showToastLong("TODO: force re-fetch data")
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
                    // TODO Prefetch data for next page
                }
            }
        })
    }

    interface OpenDetailCallback {
        fun click(item: SurveyItem)
    }
}
