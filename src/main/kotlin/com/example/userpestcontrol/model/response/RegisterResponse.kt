package com.example.userpestcontrol.model.response

data class RegisterResponse(
    val idEmployee: Long?,
    val name: String?,
    val email: String?,
    var activeDate: Long?
)