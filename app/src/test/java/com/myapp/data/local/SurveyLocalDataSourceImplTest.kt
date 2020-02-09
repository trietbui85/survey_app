package com.myapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.SurveyDao
import com.myapp.data.local.db.SurveyDatabase
import com.myapp.data.local.db.SurveyEntity
import com.myapp.ui.SurveyApp
import com.myapp.utils.TestData.surveyEntities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class SurveyLocalDataSourceImplTest {

  private lateinit var surveyDao: SurveyDao
  private lateinit var database: SurveyDatabase
  private lateinit var dataSource: SurveyLocalDataSource

  @Before
  fun setUp() {
    val app = ApplicationProvider.getApplicationContext() as SurveyApp

    // Using an in-memory database because the information stored here disappears when the
    // process is killed.
    database = Room.inMemoryDatabaseBuilder(app, SurveyDatabase::class.java)
        .allowMainThreadQueries() // // Allowing main thread queries, just for testing
        .build()
    surveyDao = database.surveyDao

    dataSource = SurveyLocalDataSourceImpl(database)

  }

  @After
  @Throws(IOException::class)
  fun tearDown() {
    database.close()
  }

  @Test
  fun makeSureCanRunQueryInDb() {
    runBlocking {
      surveyDao.loadAll()
    }
  }

  @Test
  fun loadAll_HasNoDataInDb_ReturnEmptyList() {
    runBlocking {
      surveyDao.deleteAll()
      val surveys: List<SurveyEntity> = surveyDao.loadAll()

      assertThat(surveys).isEmpty()
    }
  }

  @Test
  fun loadAll_HasDataInDb_ReturnList() {
    runBlocking {
      surveyDao.deleteAll()
      surveyDao.insertItems(surveyEntities)
      val surveys: List<SurveyEntity> = surveyDao.loadAll()

      assertThat(surveys).isNotNull()
      assertThat(surveys).hasSize(surveyEntities.size)
      assertThat(surveys).containsExactlyElementsIn(surveyEntities)
    }
  }

  @Test
  fun insertItems_HasNoDataInDb() {
    runBlocking {
      surveyDao.deleteAll()
      surveyDao.insertItems(surveyEntities)
      val surveys: List<SurveyEntity> = surveyDao.loadAll()

      assertThat(surveys).isNotNull()
      assertThat(surveys).hasSize(surveyEntities.size)
      assertThat(surveys).containsExactlyElementsIn(surveyEntities)
    }
  }

  @Test
  fun insertItems_HasExistingDataInDb() {
    runBlocking {
      surveyDao.deleteAll()
      surveyDao.insertItems(listOf(surveyEntities.first()))
      surveyDao.insertItems(surveyEntities.takeLast(surveyEntities.size - 1))

      val surveys: List<SurveyEntity> = surveyDao.loadAll()
      assertThat(surveys).isNotNull()
      assertThat(surveys).hasSize(surveyEntities.size)
      assertThat(surveys).containsExactlyElementsIn(surveyEntities)
    }
  }

  @Test
  fun insertItems_DuplicatedDataInDb() {
    runBlocking {
      surveyDao.deleteAll()
      surveyDao.insertItems(listOf(surveyEntities.first()))
      surveyDao.insertItems(surveyEntities.takeLast(surveyEntities.size - 1))

      val surveys: List<SurveyEntity> = surveyDao.loadAll()
      assertThat(surveys).isNotNull()
      assertThat(surveys).hasSize(surveyEntities.size)
      assertThat(surveys).containsExactlyElementsIn(surveyEntities)
    }
  }

  @Test
  fun `DeleteAll will make table Survey empty`() {
    runBlocking {
      surveyDao.insertItems(surveyEntities)
      surveyDao.deleteAll()
      val surveys: List<SurveyEntity> = surveyDao.loadAll()
      assertThat(surveys).isEmpty()
    }
  }

}