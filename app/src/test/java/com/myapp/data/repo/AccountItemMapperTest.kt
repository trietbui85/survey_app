package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.remote.model.AccessTokenResponse
import com.myapp.di.NetworkModule.Companion.DEFAULT_GSON
import com.myapp.utils.TestData.testJsonToken
import com.myapp.utils.TestData.testJsonTokenEmpty
import com.myapp.utils.TestData.testTokenEntity
import com.myapp.utils.TestData.testTokenItem
import com.myapp.utils.TestData.testTokenResponse
import org.junit.Before
import org.junit.Test

class AccountItemMapperTest {

  private lateinit var mapper: AccountItemMapper

  @Before
  fun setUp() {
    mapper = AccountItemMapper(
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
    val item: AccessTokenItem = mapper.fromAccessTokenResponse(testTokenResponse)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(testTokenItem)
  }

  @Test
  fun fromAccessTokenEntityToItem_EmptyEntity_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenEntity(AccessTokenEntity())
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(AccessTokenItem())
  }

  @Test
  fun fromAccessTokenEntityToItem_ValidEntity_Success() {
    val item: AccessTokenItem = mapper.fromAccessTokenEntity(testTokenEntity)
    assertThat(item).isNotNull()
    assertThat(item).isEqualTo(testTokenItem)
  }

  @Test
  fun fromAccessTokenItemToEntity_EmptyItem_Success() {
    val entity: AccessTokenEntity = mapper.toAccessTokenEntity(AccessTokenItem())
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(AccessTokenEntity())
  }

  @Test
  fun fromAccessTokenItemToEntity_ValidItem_Success() {
    val entity: AccessTokenEntity = mapper.toAccessTokenEntity(testTokenItem)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(testTokenEntity)
  }

  @Test
  fun fromAccessTokenEntityToString_EmptyEntity_ReturnJsonWithEmptyData() {
    val strToken = mapper.fromAccessTokenEntityToString(AccessTokenEntity())
    assertThat(strToken).isEqualTo(testJsonTokenEmpty)
  }

  @Test
  fun fromAccessTokenEntityToString_ValidEntity_ReturnJsonData() {
    val strToken = mapper.fromAccessTokenEntityToString(testTokenEntity)
    assertThat(strToken).isEqualTo(testJsonToken)
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
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(testJsonTokenEmpty)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(AccessTokenEntity())
  }

  @Test
  fun fromStringToAccessTokenEntity_ValidJsonData_ReturnEntity() {
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(testJsonToken)
    assertThat(entity).isNotNull()
    assertThat(entity).isEqualTo(testTokenEntity)
  }
}