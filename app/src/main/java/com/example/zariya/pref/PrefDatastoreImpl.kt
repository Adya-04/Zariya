package com.example.zariya.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.zariya.utils.Constants.AUTH_TOKEN
import com.example.zariya.utils.Constants.USER_ROLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefDatastoreImpl @Inject constructor(private val datastore : DataStore<Preferences>) : PrefDatastore {

    override suspend fun saveToken(token: String) {
        datastore.edit {
            it[stringPreferencesKey(AUTH_TOKEN)] = token
        }
    }

    override fun getToken(): Flow<String> {
        return datastore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[stringPreferencesKey(AUTH_TOKEN)]?:""
        }
    }

    override suspend fun clearToken() {
        TODO("Not yet implemented")
    }

    override suspend fun saveRole(role: String) {
        datastore.edit {
            it[stringPreferencesKey(USER_ROLE)] = role
        }
    }

    override fun getRole(): Flow<String> {
        return datastore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[stringPreferencesKey(USER_ROLE)]?:""
        }
    }

    override suspend fun clearRole() {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }
}