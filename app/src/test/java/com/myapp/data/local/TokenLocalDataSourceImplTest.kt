package com.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.repo.AccountDataMapper
import com.myapp.ui.SurveyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
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
    dataSource = TokenLocalDataSourceImpl(pref, mapper = AccountDataMapper(DEFAULT_GSON))

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
  fun loadTokenFromCache_InvalidDataInSharedPref_ReturnNull() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref("{xx}")
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
    assertThat(entity).isNull()
  }

  @Test
  fun loadTokenFromCache_ValidDataInSharedPref_ReturnSuccess() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref(STRING_TOKEN_DATA)
    val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()

    assertThat(entity).isNotNull()
    assertThat(entity!!.accessToken).isEqualTo(TOKEN_VALUE)
    assertThat(entity.tokenType).isEqualTo(TOKEN_TYPE)
    assertThat(entity.expiresIn).isEqualTo(EXPIRED_IN)
    assertThat(entity.createdAt).isEqualTo(CREATED_AT)
  }

  @Test
  fun removeTokenFromCache_HasDataInSharedPref_ReturnSuccess() = runBlockingTest(testDispatcher) {
    setTextDataForSharedPref(STRING_TOKEN_DATA)
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
      dataSource.saveTokenToCache(DEFAULT_TOKEN_ENTITY)

      val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
      assertThat(entity).isNotNull()
      assertThat(entity!!.accessToken).isEqualTo(TOKEN_VALUE)
      assertThat(entity.tokenType).isEqualTo(TOKEN_TYPE)
      assertThat(entity.expiresIn).isEqualTo(EXPIRED_IN)
      assertThat(entity.createdAt).isEqualTo(CREATED_AT)
    }

  @Test
  fun saveTokenToCache_HasDataInSharedPref_ReturnSuccess() =
    runBlockingTest(testDispatcher) {
      setTextDataForSharedPref(STRING_TOKEN_DATA)
      dataSource.saveTokenToCache(DEFAULT_TOKEN_ENTITY)

      val entity: AccessTokenEntity? = dataSource.loadTokenFromCache()
      assertThat(entity).isNotNull()
      assertThat(entity!!.accessToken).isEqualTo(TOKEN_VALUE)
      assertThat(entity.tokenType).isEqualTo(TOKEN_TYPE)
      assertThat(entity.expiresIn).isEqualTo(EXPIRED_IN)
      assertThat(entity.createdAt).isEqualTo(CREATED_AT)
    }

  companion object {
    private val DEFAULT_GSON = GsonBuilder().setFieldNamingPolicy(
        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
    )
        .create()

    private const val TOKEN_VALUE = "token_data"
    private const val TOKEN_TYPE = "token_type"
    private const val EXPIRED_IN = 2
    private const val CREATED_AT = 1

    private val STRING_TOKEN_DATA = """
            {"access_token":"$TOKEN_VALUE","token_type":"$TOKEN_TYPE","expires_in":$EXPIRED_IN,"created_at":$CREATED_AT}
        """.trimIndent()

    private val DEFAULT_TOKEN_ENTITY = AccessTokenEntity(
        accessToken = TOKEN_VALUE,
        tokenType = TOKEN_TYPE,
        expiresIn = EXPIRED_IN,
        createdAt = CREATED_AT
    )
  }
}