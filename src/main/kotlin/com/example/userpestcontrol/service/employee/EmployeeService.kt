package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.domain.Employee
import com.example.userpestcontrol.domain.request.RegisterRequest

interface EmployeeService {

    fun register(registerRequest: RegisterRequest): Employee?

    fun findUserByEmail(email: String?): Employee?

    fun findUserByIdEmployee(idEmployee: Long): Employee?
}