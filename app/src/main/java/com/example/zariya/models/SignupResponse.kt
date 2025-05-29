package com.example.zariya.models

data class SignupResponse(
    val success: Boolean,
    val message: String,
    val token: String
)