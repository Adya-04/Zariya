package com.example.zariya.models

data class CreateApplicationResponse(
    val application: Application2,
    val message: String,
    val success: Boolean
)

data class Application2(
    val __v: Int,
    val _id: String,
    val address: String,
    val age: Int,
    val appliedBy: String,
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