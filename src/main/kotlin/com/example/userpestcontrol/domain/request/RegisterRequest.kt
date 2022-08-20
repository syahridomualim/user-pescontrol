package com.example.userpestcontrol.domain.request

data class RegisterRequest(
    val idEmployee: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)