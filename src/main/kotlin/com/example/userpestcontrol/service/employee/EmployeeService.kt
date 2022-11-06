package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.entity.Employee
import com.example.userpestcontrol.model.request.RegisterRequest

interface EmployeeService {

    fun register(registerRequest: RegisterRequest): Employee?

    fun findUserByEmail(email: String?): Employee?

    fun findUserByIdEmployee(idEmployee: Long): Employee?

    fun getEmployees(): List<Employee>?

    fun sendLink(email: String?)

    fun resetPassword(email: String?, newPassword: String?)
}