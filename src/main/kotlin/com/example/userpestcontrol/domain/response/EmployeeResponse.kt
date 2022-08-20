package com.example.userpestcontrol.domain.response

data class EmployeeResponse(
    val id: Long,
    val idEmployee: Long,
    var name: String,
    var email: String,
    var profileImageUrl: String,
    var activeDate: Long,
    var lastLoginDate: Long,
    var lastLoginDisplayDate: Long,
)
