package com.myapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myapp.R
import com.myapp.data.repo.SurveyItem
import com.myapp.utils.showToastLong
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.detail_fragment.*
import javax.inject.Inject

class DetailFragment : DaggerFragment() {

    companion object {
        const val EXTRA_SURVEY_ITEM = "EXTRA_SURVEY_ITEM"
        fun newInstance() = DetailFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Suppress("unused")
    private val viewModel by viewModels<DetailViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val surveyItem: SurveyItem? = arguments?.getParcelable(EXTRA_SURVEY_ITEM)

        if (surveyItem == null) {
            this.showToastLong(R.string.survey_details_invalid)
            goBack()
            return;
        }

        tvContent.text = surveyItem.title
        ivBack.setOnClickListener { goBack() }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}
