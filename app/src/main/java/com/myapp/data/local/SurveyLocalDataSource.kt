package com.myapp.data.local

import com.myapp.data.local.db.SurveyDatabase
import com.myapp.data.local.db.SurveyEntity

// DbDataSource is the interface to let repository interact with database
interface SurveyLocalDataSource {
    // Insert list of surveys into database
    suspend fun insertItems(items: List<SurveyEntity>)

    // Get the list of surveys from database
    suspend fun loadAll(): List<SurveyEntity>

    // Delete all surveys in database
    suspend fun deleteAll()
}

class SurveyLocalDataSourceImpl(private val database: SurveyDatabase) : SurveyLocalDataSource {
    override suspend fun insertItems(items: List<SurveyEntity>) {
        return database.surveyDao.insertItems(items)
    }

    override suspend fun loadAll(): List<SurveyEntity> {
        return database.surveyDao.loadAll()
    }

    override suspend fun deleteAll() {
        return database.surveyDao.deleteAll()
    }

}