package com.example.userpestcontrol.model.request

data class CoordinatorRegisterRequest(
    val idEmployee: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)