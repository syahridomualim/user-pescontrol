package com.example.userpestcontrol.domain.response

data class RegisterResponse(
    val idEmployee: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    var activeDate: String? = null,
)