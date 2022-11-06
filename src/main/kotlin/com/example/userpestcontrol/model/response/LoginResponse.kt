package com.example.userpestcontrol.model.response

data class LoginResponse(
    val id: Long,
    val idEmployee: Long,
    val name: String,
    val token: String,
)