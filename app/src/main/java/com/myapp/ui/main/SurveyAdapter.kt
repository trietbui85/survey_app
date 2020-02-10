package com.myapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.myapp.R
import com.myapp.data.repo.SurveyItem
import kotlinx.android.synthetic.main.item_survey.view.coverImageView
import kotlinx.android.synthetic.main.item_survey.view.descriptionTextView
import kotlinx.android.synthetic.main.item_survey.view.nameTextView
import kotlinx.android.synthetic.main.item_survey.view.takeSurveyButton

class SurveyAdapter(private val callback: MainFragment.OpenDetailCallback) :
  RecyclerView.Adapter<PagerVH>() {

  private var surveyItems = mutableListOf<SurveyItem>()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PagerVH =
    PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_survey, parent, false))

  override fun getItemCount(): Int = surveyItems.size

  override fun onBindViewHolder(
    holder: PagerVH,
    position: Int
  ) = holder.itemView.run {
    val surveyItem = surveyItems[position]

    nameTextView.text = surveyItem.title
    descriptionTextView.text = surveyItem.description
    if (surveyItem.coverHighResImageUrl.isNotEmpty()) {
      coverImageView.load(surveyItem.coverHighResImageUrl)
    }
    takeSurveyButton.setOnClickListener {
      callback.click(surveyItem)
    }
  }

  fun setItems(list: MutableList<SurveyItem>) {
    this.surveyItems = list
    notifyDataSetChanged()
  }

  fun clearItems() {
    surveyItems.clear()
    notifyDataSetChanged()
  }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)