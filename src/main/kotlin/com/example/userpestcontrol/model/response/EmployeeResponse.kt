package com.example.userpestcontrol.model.response

import com.example.userpestcontrol.entity.Area
import com.example.userpestcontrol.entity.Department

data class EmployeeResponse(
    val idEmployee: Long,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    val activeDate: Long?,
    val role: String?,
    val authorities: Array<out String>?,
    val lastLoginDate: Long?,
    val lastLoginDisplayDate: Long?,
    val isActive: Boolean,
    val isNotLocked: Boolean,
    val area: Area?,
    val department: Department?
)