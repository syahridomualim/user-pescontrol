package com.example.userpestcontrol.repository

import com.example.userpestcontrol.entity.Department
import org.springframework.data.jpa.repository.JpaRepository

interface DepartmentRepository : JpaRepository<Department, Long> {

    fun findByName(name: String): Department?

}