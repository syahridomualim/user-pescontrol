package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.entity.Employee
import com.example.userpestcontrol.model.request.CoordinatorRegisterRequest
import com.example.userpestcontrol.model.request.EmployeeRegisterRequest

interface EmployeeService {

    fun registerEmployee(employeeRegisterRequest: EmployeeRegisterRequest): Employee?

    fun registerCoordinator(coordinatorRegisterRequest: CoordinatorRegisterRequest): Employee?

    fun findUserByEmail(email: String?): Employee?

    fun findUserByIdEmployee(idEmployee: Long): Employee?

    fun getEmployees(): List<Employee>?

    fun sendLink(email: String?)

    fun resetPassword(email: String?, newPassword: String?)
}