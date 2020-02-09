package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.remote.model.AccessTokenResponse
import com.myapp.di.NetworkModule.Companion.DEFAULT_GSON
import com.myapp.utils.TestData.jsonTokenData
import com.myapp.utils.TestData.jsonTokenEmpty
import com.myapp.utils.TestData.tokenEntity
import com.myapp.utils.TestData.tokenItem
import com.myapp.utils.TestData.tokenResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountDataMapperTest {

  private lateinit var mapper: AccountDataMapper

  @Before
  fun setUp() {
    mapper = AccountDataMapper(
      gson = DEFAULT_GSON
    )
  }

  @Test
  fun fromAccessTokenResponseToItem_EmptyResponse_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenResponse(AccessTokenResponse())

    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(AccessTokenItem())
  }

  @Test
  fun fromAccessTokenResponseToItem_ValidResponse_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenResponse(tokenResponse)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(tokenItem)
  }

  @Test
  fun fromAccessTokenEntityToItem_EmptyEntity_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenEntity(AccessTokenEntity())
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(AccessTokenItem())
  }

  @Test
  fun fromAccessTokenEntityToItem_ValidEntity_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenEntity(tokenEntity)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(tokenItem)
  }

  @Test
  fun fromAccessTokenItemToEntity_EmptyItem_Success() {
    val entity: AccessTokenEntity = mapper.toAccessTokenEntity(AccessTokenItem())
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(AccessTokenEntity())
  }

  @Test
  fun fromAccessTokenItemToEntity_ValidItem_Success() {
    val entity: AccessTokenEntity = mapper.toAccessTokenEntity(tokenItem)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(tokenEntity)
  }

  @Test
  fun fromAccessTokenEntityToString_EmptyEntity_ReturnJsonWithEmptyData() {
    val strToken = mapper.fromAccessTokenEntityToString(AccessTokenEntity())
    assertThat(strToken).isEqualTo(jsonTokenEmpty)
  }

  @Test
  fun fromAccessTokenEntityToString_ValidEntity_ReturnJsonData() {
    val strToken = mapper.fromAccessTokenEntityToString(tokenEntity)
    assertThat(strToken).isEqualTo(jsonTokenData)
  }

  @Test
  fun fromStringToAccessTokenEntity_NullOrEmptyString_ReturnNull() {
    var entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(null)
    assertThat(entity).isNull()

    entity = mapper.fromStringToAccessTokenEntity("")
    assertThat(entity).isNull()

    entity = mapper.fromStringToAccessTokenEntity("  ")
    assertThat(entity).isNull()
  }

  @Test
  fun fromStringToAccessTokenEntity_InvalidJsonText_ReturnNull() {
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity("123")
    assertThat(entity).isNull()

    val entity2: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity("{x}")
    assertThat(entity2).isNull()
  }

  @Test
  fun fromStringToAccessTokenEntity_JsonWithEmptyData_ReturnEmptyEntity() {
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(jsonTokenEmpty)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(AccessTokenEntity())
  }

  @Test
  fun fromStringToAccessTokenEntity_ValidJsonData_ReturnEntity() {
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(jsonTokenData)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(tokenEntity)
  }
}