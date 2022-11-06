package com.example.userpestcontrol.repository

import com.example.userpestcontrol.entity.Presensi
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PresensiRepository : JpaRepository<Presensi, Long> {

    @Query(value = "SELECT p FROM Presensi p WHERE p.employee.idEmployee = :id_employee and p.timeOut IS NULL")
    fun findEmployeeByIdEmployee(@Param("id_employee") idEmployee: Long): Presensi

    @Query(value = "SELECT p FROM Presensi p WHERE p.employee.idEmployee = :id_employee")
    fun findListEmployeeByIdEmployee(@Param("id_employee") idEmployee: Long): List<Presensi>?

}