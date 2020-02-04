package com.myapp.data.repo

import com.myapp.data.local.db.SurveyEntity
import com.myapp.data.remote.model.SurveyResponse

class SurveyDataMapper() {

    fun fromSurveyResponse(response: SurveyResponse) = SurveyItem(
        id = response.id,
        title = response.title,
        description = response.description,
        coverImageUrl = response.coverImageUrl
    )

    fun fromSurveyEntity(entity: SurveyEntity) = SurveyItem(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        coverImageUrl = entity.coverImageUrl
    )

    fun toSurveyEntity(item: SurveyItem) = SurveyEntity(
        id = item.id,
        title = item.title,
        description = item.description,
        coverImageUrl = item.coverImageUrl
    )
}