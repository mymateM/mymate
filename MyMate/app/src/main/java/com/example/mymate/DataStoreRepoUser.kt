package com.example.mymate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepoUser(private val dataStore: DataStore<Preferences>) {

    companion object {
        val USER_ACCESS_KEY = stringPreferencesKey("User_Access_key")
        val USER_REFRESH_KEY = stringPreferencesKey("User_Refresh_key")
    }

    suspend fun keyUser(accesskey: String, refreshkey: String) {
        dataStore.edit {
            it[USER_ACCESS_KEY] = accesskey
            it[USER_REFRESH_KEY] = refreshkey
        }
    }

    val userAccessFlow: Flow<String?> = dataStore.data.map {
        it[USER_ACCESS_KEY]
    }

    val userRefreshFlow: Flow<String?> = dataStore.data.map {
        it[USER_REFRESH_KEY]
    }

}