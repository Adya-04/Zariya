package com.example.zariya.pref

import kotlinx.coroutines.flow.Flow

interface PrefDatastore {

    suspend fun saveToken(token: String)

    fun getToken(): Flow<String>

    suspend fun clearToken()

    suspend fun saveRole(role: String)

    fun getRole(): Flow<String>

    suspend fun clearRole()

    suspend fun clearAll()
}