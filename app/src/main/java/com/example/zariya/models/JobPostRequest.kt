package com.example.zariya.models

data class JobPostRequest(
    val contact: String,
    val description: String,
    val salary: String,
    val skillsRequired: String,
    val title: String,
    val workType: String,
    val workingHours: String
)