package com.example.zariya.models

data class JobGetForRecruitersResponse(
    val jobs: List<JobX>,
    val success: Boolean
)

data class JobX(
    val __v: Int,
    val _id: String,
    val applicants: List<Any>,
    val companyId: CompanyId,
    val contact: String,
    val createdAt: String,
    val createdby: String,
    val description: String,
    val salary: String,
    val skillsRequired: String,
    val title: String,
    val updatedAt: String,
    val workType: String,
    val workingHours: String
)