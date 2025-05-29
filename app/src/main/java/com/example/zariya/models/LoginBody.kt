package com.example.zariya.models

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String
)
