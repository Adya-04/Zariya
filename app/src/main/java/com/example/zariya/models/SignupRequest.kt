package com.example.zariya.models

data class SignupRequest(
    val fullname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val role: String
)