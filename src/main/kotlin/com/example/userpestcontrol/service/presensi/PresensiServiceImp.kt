package com.example.userpestcontrol.service.presensi

import com.example.userpestcontrol.entity.Presensi
import com.example.userpestcontrol.exception.domain.CheckedInException
import com.example.userpestcontrol.repository.EmployeeRepository
import com.example.userpestcontrol.repository.PresensiRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@Qualifier("userDetails")
class PresensiServiceImp @Autowired constructor(
    private val employeeRepository: EmployeeRepository,
    private val presensiRepository: PresensiRepository,
) : PresensiService {

    override fun savePresensi(presensi: Presensi): Presensi {
        return presensiRepository.save(presensi)
    }

    override fun checkIn(idEmployee: Long, note: String, location: String): Presensi? {
        val employee = employeeRepository.findUserByIdEmployee(idEmployee)
        val currentPresensi = presensiRepository.findListEmployeeByIdEmployee(idEmployee)

        if (currentPresensi?.lastOrNull()?.id == null) {
            val presensi = Presensi(
                employee = employee!!,
                date = Date().time,
                timeIn = Date().time,
                timeOut = null,
                note = note,
                locationIn = location,
                locationOut = null
            )

            presensiRepository.save(presensi)
        }

        if (currentPresensi?.lastOrNull()?.timeOut == null) {
            throw CheckedInException("You have checked in")
        }

        val presensi = Presensi(
            employee = employee!!,
            date = Date().time,
            timeIn = Date().time,
            timeOut = null,
            note = note,
            locationIn = location,
            locationOut = null
        )
        presensiRepository.save(presensi)

        return currentPresensi.lastOrNull()
    }

    override fun checkOut(idEmployee: Long,  location: String): Presensi? {
        val currentPresensi = presensiRepository.findListEmployeeByIdEmployee(idEmployee)?.lastOrNull()

        if (currentPresensi?.timeOut != null) {
            throw CheckedInException("You have to check in")
        }

        val presensi = findPresensiByIdEmployee(idEmployee)
        presensi?.timeOut = Date().time
        presensi?.locationOut = location
        return presensiRepository.save(presensi as Presensi)
    }

    override fun findPresensiByIdEmployee(idEmployee: Long): Presensi? {
        return presensiRepository.findEmployeeByIdEmployee(idEmployee)
    }

    override fun findListEmployeeByIdEmployee(idEmployee: Long): List<Presensi>? {
        return presensiRepository.findListEmployeeByIdEmployee(idEmployee)
    }

    override fun findAllPresensi(): List<Presensi> {
        return presensiRepository.findAll()
    }


}