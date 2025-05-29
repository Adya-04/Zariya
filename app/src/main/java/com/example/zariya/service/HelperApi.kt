package com.example.zariya.service

import com.example.zariya.models.CreateApplicationRequest
import com.example.zariya.models.CreateApplicationResponse
import com.example.zariya.models.JobGetforHelperResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HelperApi {

    @GET("job/all")
    suspend fun getJobsForHelpers(
        @Header("Authorization") token: String
    ) : Response<JobGetforHelperResponse>

    @POST("application/create")
    suspend fun createApplication(
        @Header("Authorization") token: String,
        @Body request: CreateApplicationRequest
    ): Response<CreateApplicationResponse>
}