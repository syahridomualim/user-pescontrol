package com.example.userpestcontrol.model.request

import com.example.userpestcontrol.entity.Area

data class EmployeeRegisterRequest(
    val idEmployee: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val area: Area
)