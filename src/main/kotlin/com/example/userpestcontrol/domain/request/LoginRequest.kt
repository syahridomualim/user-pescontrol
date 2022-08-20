package com.example.userpestcontrol.domain.request

data class LoginRequest(
    val email: String,
    val password: String,
)