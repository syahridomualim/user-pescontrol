package com.example.userpestcontrol.service.presensi

import com.example.userpestcontrol.entity.Presensi

interface PresensiService {

    fun savePresensi(presensi: Presensi): Presensi

    fun checkIn(idEmployee: Long, note: String, location: String): Presensi?

    fun checkOut(idEmployee: Long, location: String): Presensi?

    fun findPresensiByIdEmployee(idEmployee: Long): Presensi?

    fun findListEmployeeByIdEmployee(idEmployee: Long): List<Presensi>?

    fun findAllPresensi(): List<Presensi>

}