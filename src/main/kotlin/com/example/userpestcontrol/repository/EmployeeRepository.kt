package com.example.userpestcontrol.repository

import com.example.userpestcontrol.domain.Employee
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository : JpaRepository<Employee, Long> {

    fun findUserByIdEmployee(idEmployee: Long?): Employee?

    fun findUserByEmail(email: String?): Employee?

}