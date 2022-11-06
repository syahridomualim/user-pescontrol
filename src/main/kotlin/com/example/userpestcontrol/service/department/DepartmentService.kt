package com.example.userpestcontrol.service.department

import com.example.userpestcontrol.entity.Department

interface DepartmentService {

    fun saveDepartment(department: Department): Department

    fun addDepartmentToEmployee(idEmployee: Long, departmentName: String)

}