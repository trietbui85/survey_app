package com.myapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.SurveyDao
import com.myapp.data.local.db.SurveyDatabase
import com.myapp.data.local.db.SurveyEntity
import com.myapp.ui.SurveyApp
import com.myapp.utils.TestData.testSurveyEntities
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
      val surveys: List<SurveyEntity> = surveyDao.let {
        it.deleteAll()
        it.insertItems(testSurveyEntities)
        return@let it.loadAll()
      }

      assertThat(surveys).let {
        it.isNotNull()
        it.hasSize(testSurveyEntities.size)
        it.containsExactlyElementsIn(testSurveyEntities)
      }
    }
  }

  @Test
  fun insertItems_HasNoDataInDb() {
    runBlocking {
      val surveys: List<SurveyEntity> = surveyDao.let {
        it.deleteAll()
        it.insertItems(testSurveyEntities)
        return@let it.loadAll()
      }

      assertThat(surveys).let {
        it.isNotNull()
        it.hasSize(testSurveyEntities.size)
        it.containsExactlyElementsIn(testSurveyEntities)
      }
    }
  }

  @Test
  fun insertItems_HasExistingDataInDb() {
    runBlocking {

      val surveys: List<SurveyEntity> = surveyDao.let {
        it.deleteAll()
        it.insertItems(listOf(testSurveyEntities.first()))
        it.insertItems(testSurveyEntities.takeLast(testSurveyEntities.size - 1))
        return@let it.loadAll()
      }

      assertThat(surveys).let {
        it.isNotNull()
        it.hasSize(testSurveyEntities.size)
        it.containsExactlyElementsIn(testSurveyEntities)
      }
    }
  }

  @Test
  fun insertItems_DuplicatedDataInDb() {
    runBlocking {
      val surveys: List<SurveyEntity> = surveyDao.let {
        it.deleteAll()
        it.insertItems(listOf(testSurveyEntities.first(), testSurveyEntities.last()))
        it.insertItems(testSurveyEntities.takeLast(testSurveyEntities.size - 1))
        return@let it.loadAll()
      }

      assertThat(surveys).let {
        it.isNotNull()
        it.hasSize(testSurveyEntities.size)
        it.containsExactlyElementsIn(testSurveyEntities)
      }
    }
  }

  @Test
  fun `DeleteAll will make table Survey empty`() {
    runBlocking {
      val surveys: List<SurveyEntity> = surveyDao.let {
        it.insertItems(testSurveyEntities)
        it.deleteAll()
        return@let it.loadAll()
      }
      assertThat(surveys).isEmpty()
    }
  }

}