package com.example.zariya.service

import com.example.zariya.models.CompanyRegResponse
import com.example.zariya.models.CompanyRegisRequest
import com.example.zariya.models.LoginRequest
import com.example.zariya.models.LoginResponse
import com.example.zariya.models.SignupRequest
import com.example.zariya.models.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("user/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("user/register")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("company/register")
    suspend fun registerCompany(
        @Header("Authorization") token: String,
        @Body request: CompanyRegisRequest
    ): Response<CompanyRegResponse>
}