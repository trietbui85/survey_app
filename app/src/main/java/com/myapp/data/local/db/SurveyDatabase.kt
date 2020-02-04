package com.myapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SurveyEntity::class], version = 1, exportSchema = false)
abstract class SurveyDatabase : RoomDatabase() {
    abstract val surveyDao: SurveyDao
}