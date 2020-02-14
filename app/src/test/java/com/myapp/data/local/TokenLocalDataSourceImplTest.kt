package com.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.repo.AccountItemMapper
import com.myapp.ui.SurveyApp
import com.myapp.utils.TestData.testJsonToken
import com.myapp.utils.TestData.testTokenEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class TokenLocalDataSourceImplTest {

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var dataSource: TokenLocalDataSource
  private lateinit var pref: SharedPreferences

  @Before
  fun setUp() {
    val app = ApplicationProvider.getApplicationContext() as SurveyApp
    pref = app.getSharedPreferences("survey", Context.MODE_PRIVATE)
    dataSource = TokenLocalDataSourceImpl(pref, mapper = AccountItemMapper())

    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  private fun clearTextDataForSharedPref() {
    pref.edit()
      .clear()
      .apply()
  }

  private fun setTextDataForSharedPref(text: String?) {
    if (text.isNullOrBlank()) {
      clearTextDataForSharedPref()
      return
    }
    pref.edit()
      .putString(TokenLocalDataSource.KEY_ACCESS_TOKEN, text)
      .apply()
  }

  @Test
  fun loadTokenFromCache_NoDataInSharedPref_ReturnNull() = runBlockingTest(testDispatcher) {
    clearTextDataForSharedPref()
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
    assertThat(entity).isNull()
  }

  @Test
  fun loadTokenFromCache_NullDataInSharedPref_ReturnNull() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref(null)
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
    assertThat(entity).isNull()
  }

  @Test
  fun loadTokenFromCache_EmptyDataInSharedPref_ReturnNull() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref("  ")
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
    assertThat(entity).isNull()
  }

  @Test
  fun loadTokenFromCache_ValidDataInSharedPref_ReturnSuccess() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref("123")
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()

    assertThat(entity).isNotNull()
    assertThat(entity!!.accessToken).isEqualTo("123")
  }

  @Test
  fun removeTokenFromCache_HasDataInSharedPref_ReturnSuccess() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref(testJsonToken)
    dataSource.removeTokenFromCache()

    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
    assertThat(entity).isNull()
  }

  @Test
  fun removeTokenFromCache_HasNoDataInSharedPref_ReturnSuccess() =
    runBlockingTest(testDispatcher) {
      clearTextDataForSharedPref()
      dataSource.removeTokenFromCache()

      val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
      assertThat(entity).isNull()
    }

  @Test
  fun saveTokenToCache_HasNoDataInSharedPref_ReturnSuccess() =
    runBlockingTest(testDispatcher) {
      clearTextDataForSharedPref()
      dataSource.saveTokenToCache(testTokenEntity)

      val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
      assertThat(entity).isNotNull()
      assertThat(entity).isEqualTo(testTokenEntity)
    }

  @Test
  fun saveTokenToCache_HasDataInSharedPref_ReturnSuccess() =
    runBlockingTest(testDispatcher) {
      setTextDataForSharedPref(testJsonToken)
      dataSource.saveTokenToCache(testTokenEntity)

      val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
      assertThat(entity).isNotNull()
      assertThat(entity).isEqualTo(testTokenEntity)
    }

}