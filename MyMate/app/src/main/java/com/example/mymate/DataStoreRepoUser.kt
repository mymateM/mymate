package com.example.mymate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepoUser(private val dataStore: DataStore<Preferences>) {

    companion object {
        val USER_ACCESS_KEY = stringPreferencesKey("User_Access_key")
        val USER_REFRESH_KEY = stringPreferencesKey("User_Refresh_key")
        val USER_DEVICE_KEY = stringPreferencesKey("User_Device_key")
    }

    suspend fun keyUser(accesskey: String, refreshkey: String) {
        dataStore.edit {
            it[USER_ACCESS_KEY] = accesskey
            it[USER_REFRESH_KEY] = refreshkey
        }
    }

    suspend fun keyDevice(devicekey: String) {
        dataStore.edit {
            it[USER_DEVICE_KEY] = devicekey
        }
    }

    suspend fun getAccessToken(): String {
        val accesskey: Flow<String> = dataStore.data
            .catch {exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[USER_ACCESS_KEY] ?: ""
            }
        return accesskey.toString()
    }

    suspend fun getRefreshKey(): String {
        val refreshkey: Flow<String> = dataStore.data
            .catch {exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[USER_REFRESH_KEY] ?: ""
            }
        return refreshkey.toString()
    }

    suspend fun getDeviceKey(): String {
        val devicekey: Flow<String> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[USER_DEVICE_KEY] ?: ""
            }
        return devicekey.toString()
    }

    val userAccessFlow: Flow<String?> = dataStore.data.map {
        it[USER_ACCESS_KEY]
    }

    val userRefreshFlow: Flow<String?> = dataStore.data.map {
        it[USER_REFRESH_KEY]
    }

    val userAccessReadFlow: Flow<String?> = dataStore.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
        preferences ->  preferences[USER_ACCESS_KEY] ?: ""
    }

    val userRefreshReadFlow: Flow<String?> = dataStore.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
        preferences -> preferences[USER_REFRESH_KEY] ?: ""
    }

    val userDeviceReadFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            preferences -> preferences[USER_DEVICE_KEY] ?: ""
        }

}