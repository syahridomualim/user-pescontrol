package com.example.userpestcontrol.domain.response

data class LoginResponse(
    val id: Long,
    val idEmployee: Long,
    val name: String,
    val token: String,
)