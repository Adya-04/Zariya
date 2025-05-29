package com.example.zariya.service

import com.example.zariya.models.ApplicationsResponse
import com.example.zariya.models.JobGetForRecruitersResponse
import com.example.zariya.models.JobPostRequest
import com.example.zariya.models.JobPostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RecruiterApi {

    @POST("job/post")
    suspend fun postJob(
        @Header("Authorization") token: String,
        @Body request: JobPostRequest) : Response<JobPostResponse>

    @GET("job/recruiter")
    suspend fun getJobsForRecruiters(
        @Header("Authorization") token: String
    ): Response<JobGetForRecruitersResponse>

    @GET("application/job/{jobId}/applied")
    suspend fun getAppliedCandidates(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: String
    ): Response<ApplicationsResponse>

    @GET("application/job/{jobId}/approved")
    suspend fun getApprovedCandidates(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: String
    ): Response<ApplicationsResponse>

    @GET("application/job/{jobId}/hired")
    suspend fun getHiredCandidates(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: String
    ): Response<ApplicationsResponse>
}