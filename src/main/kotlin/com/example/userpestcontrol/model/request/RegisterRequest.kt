package com.example.userpestcontrol.model.request

import org.springframework.validation.annotation.Validated

data class RegisterRequest(
    val idEmployee: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)