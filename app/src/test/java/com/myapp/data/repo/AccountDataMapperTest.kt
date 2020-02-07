package com.myapp.data.repo

import com.google.common.truth.Truth.assertThat
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.remote.model.AccessTokenResponse
import org.junit.Before
import org.junit.Test

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
    val tokenItem: AccessTokenItem = mapper.fromAccessTokenResponse(AccessTokenResponse())
    assertThat(tokenItem).isNotNull()
    assertThat(tokenItem.accessToken).isEmpty()
    assertThat(tokenItem.tokenType).isEmpty()
    assertThat(tokenItem.expiresIn).isEqualTo(0)
    assertThat(tokenItem.createdAt).isEqualTo(0)
  }

  @Test
  fun fromAccessTokenResponseToItem_ValidResponse_Success() {
    val tokenItem: AccessTokenItem = mapper.fromAccessTokenResponse(DEFAULT_TOKEN_RESPONSE)
    assertThat(tokenItem).isNotNull()
    assertThat(tokenItem.accessToken).isEqualTo(TOKEN_VALUE)
    assertThat(tokenItem.tokenType).isEqualTo(TOKEN_TYPE)
    assertThat(tokenItem.expiresIn).isEqualTo(EXPIRED_IN)
    assertThat(tokenItem.createdAt).isEqualTo(CREATED_AT)
  }

  @Test
  fun fromAccessTokenEntityToItem_EmptyEntity_Success() {
    val tokenItem: AccessTokenItem = mapper.fromAccessTokenEntity(AccessTokenEntity())
    assertThat(tokenItem).isNotNull()
    assertThat(tokenItem.accessToken).isEmpty()
    assertThat(tokenItem.tokenType).isEmpty()
    assertThat(tokenItem.expiresIn).isEqualTo(0)
    assertThat(tokenItem.createdAt).isEqualTo(0)
  }

  @Test
  fun fromAccessTokenEntityToItem_ValidEntity_Success() {
    val tokenItem: AccessTokenItem = mapper.fromAccessTokenEntity(DEFAULT_TOKEN_ENTITY)
    assertThat(tokenItem).isNotNull()
    assertThat(tokenItem.accessToken).isEqualTo(TOKEN_VALUE)
    assertThat(tokenItem.tokenType).isEqualTo(TOKEN_TYPE)
    assertThat(tokenItem.expiresIn).isEqualTo(EXPIRED_IN)
    assertThat(tokenItem.createdAt).isEqualTo(CREATED_AT)
  }

  @Test
  fun fromAccessTokenItemToEntity_EmptyItem_Success() {
    val tokenEntity: AccessTokenEntity = mapper.toAccessTokenEntity(AccessTokenItem())
    assertThat(tokenEntity).isNotNull()
    assertThat(tokenEntity.accessToken).isEmpty()
    assertThat(tokenEntity.tokenType).isEmpty()
    assertThat(tokenEntity.expiresIn).isEqualTo(0)
    assertThat(tokenEntity.createdAt).isEqualTo(0)
  }

  @Test
  fun fromAccessTokenItemToEntity_ValidItem_Success() {
    val tokenEntity: AccessTokenEntity = mapper.toAccessTokenEntity(DEFAULT_TOKEN_ITEM)
    assertThat(tokenEntity).isNotNull()
    assertThat(tokenEntity.accessToken).isEqualTo(TOKEN_VALUE)
    assertThat(tokenEntity.tokenType).isEqualTo(TOKEN_TYPE)
    assertThat(tokenEntity.expiresIn).isEqualTo(EXPIRED_IN)
    assertThat(tokenEntity.createdAt).isEqualTo(CREATED_AT)
  }

  @Test
  fun fromAccessTokenEntityToString_EmptyEntity_ReturnJsonWithEmptyData() {
    val strToken = mapper.fromAccessTokenEntityToString(AccessTokenEntity())
    assertThat(strToken).isEqualTo(
        """
            {"access_token":"","token_type":"","expires_in":0,"created_at":0}
        """.trimIndent()
    )
  }

  @Test
  fun fromAccessTokenEntityToString_ValidEntity_ReturnJsonData() {
    val strToken = mapper.fromAccessTokenEntityToString(DEFAULT_TOKEN_ENTITY)
    assertThat(strToken).isEqualTo(
        """
            {"access_token":"$TOKEN_VALUE","token_type":"$TOKEN_TYPE","expires_in":$EXPIRED_IN,"created_at":$CREATED_AT}
        """.trimIndent()
    )
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
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(
        """
            {"access_token":"","token_type":"","expires_in":0,"created_at":0}
        """.trimIndent()
    )
    assertThat(entity).isNotNull()
    assertThat(entity!!.accessToken).isEmpty()
    assertThat(entity.tokenType).isEmpty()
    assertThat(entity.expiresIn).isEqualTo(0)
    assertThat(entity.createdAt).isEqualTo(0)
  }

  @Test
  fun fromStringToAccessTokenEntity_ValidJsonData_ReturnEntity() {
    val entity: AccessTokenEntity? = mapper.fromStringToAccessTokenEntity(
        """
            {"access_token":"$TOKEN_VALUE","token_type":"$TOKEN_TYPE","expires_in":$EXPIRED_IN,"created_at":$CREATED_AT}
        """.trimIndent()
    )
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

    private val DEFAULT_TOKEN_RESPONSE =
      AccessTokenResponse(
          accessToken = TOKEN_VALUE,
          tokenType = TOKEN_TYPE,
          expiresIn = EXPIRED_IN,
          createdAt = CREATED_AT
      )

    private val DEFAULT_TOKEN_ENTITY = AccessTokenEntity(
        accessToken = TOKEN_VALUE,
        tokenType = TOKEN_TYPE,
        expiresIn = EXPIRED_IN,
        createdAt = CREATED_AT
    )

    private val DEFAULT_TOKEN_ITEM = AccessTokenItem(
        accessToken = TOKEN_VALUE,
        tokenType = TOKEN_TYPE,
        expiresIn = EXPIRED_IN,
        createdAt = CREATED_AT
    )
  }
}