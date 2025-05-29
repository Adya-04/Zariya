package com.example.zariya.models

data class CreateApplicationRequest(
    val address: String,
    val age: Int,
    val contact: String,
    val education: String,
    val gender: String,
    val jobId: String,
    val name: String,
    val skills: String
)