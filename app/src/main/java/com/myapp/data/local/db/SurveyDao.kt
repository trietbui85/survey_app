package com.myapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SurveyDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItems(items: List<SurveyEntity>)

  @Query("SELECT * FROM survey")
  suspend fun loadAll(): List<SurveyEntity>

  @Query("DELETE FROM survey")
  suspend fun deleteAll()
}