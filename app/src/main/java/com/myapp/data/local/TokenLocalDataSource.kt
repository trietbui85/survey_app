package com.myapp.data.local

import android.content.SharedPreferences
import com.myapp.data.local.TokenLocalDataSource.Companion.KEY_ACCESS_TOKEN
import com.myapp.data.local.db.AccessTokenEntity
import com.myapp.data.repo.AccountDataMapper

// TokenDataSource is the interface to let repository interact with Token data (which is saved
// in SharedPreferences)
interface TokenLocalDataSource {
    fun loadTokenFromCache(): AccessTokenEntity?
    fun removeTokenFromCache()
    fun saveTokenToCache(token: AccessTokenEntity)

    companion object {
        const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}

class TokenLocalDataSourceImpl(
    private val pref: SharedPreferences,
    private val mapper: AccountDataMapper
) : TokenLocalDataSource {
    override fun loadTokenFromCache(): AccessTokenEntity? {
        val strToken = pref.getString(KEY_ACCESS_TOKEN, "")
        return mapper.fromStringToAccessTokenEntity(strToken)
    }

    override fun removeTokenFromCache() {
        pref.edit().remove(KEY_ACCESS_TOKEN).apply()
    }

    override fun saveTokenToCache(token: AccessTokenEntity) {
        val strToken = mapper.fromAccessTokenEntityToString(token)
        pref.edit().putString(KEY_ACCESS_TOKEN, strToken).apply()
    }

}