package com.example.zariya.models

data class CompanyRegResponse(
    val message: String,
    val company: Company? = null,
    val success: Boolean
)

data class Company(
    val name: String,
    val description: String,
    val location: String,
    val website: String,
    val logo: String?,
    val userid: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)