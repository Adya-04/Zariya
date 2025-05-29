package com.example.zariya.models

data class ApplicationsResponse(
    val applications: List<Application>,
    val count: Int,
    val message: String,
    val success: Boolean
)

data class Application(
    val __v: Int,
    val _id: String,
    val address: String,
    val age: Int,
    val appliedBy: AppliedBy,
    val contact: String,
    val createdAt: String,
    val education: String,
    val gender: String,
    val jobId: String,
    val name: String,
    val skills: String,
    val status: String,
    val updatedAt: String
)

data class ApplicationData(
    val name: String,
    val location: String,
    val age: Int
)