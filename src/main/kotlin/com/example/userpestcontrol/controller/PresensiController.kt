package com.example.userpestcontrol.controller

import com.example.userpestcontrol.entity.Presensi
import com.example.userpestcontrol.exception.ExceptionHandling
import com.example.userpestcontrol.model.response.PresensiResponse
import com.example.userpestcontrol.service.presensi.PresensiService
import com.example.userpestcontrol.utility.createHttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employee")
class PresensiController @Autowired constructor(
    private val presensiService: PresensiService,
) : ExceptionHandling() {

    @PostMapping("/presensi/checkIn")
    fun checkIn(
        @RequestParam(name = "idEmployee") idEmployee: Long,
        @RequestParam(name = "note") note: String,
        @RequestParam(name = "location") location: String,
    ): ResponseEntity<*> {

        presensiService.checkIn(idEmployee, note, location)

        return createHttpResponse(HttpStatus.OK, "You have checked in")
    }

    @PostMapping("/presensi/checkOut")
    fun checkOut(
        @RequestParam(name = "idEmployee") idEmployee: Long,
        @RequestParam(name = "location") location: String
    ): ResponseEntity<*> {
        presensiService.checkOut(idEmployee, location)

        return createHttpResponse(message = "You have been checked out", httpStatus = HttpStatus.OK)
    }

    @GetMapping("/presensi/{idEmployee}")
    fun checkAttendance(@PathVariable idEmployee: Long): ResponseEntity<List<PresensiResponse>> {
        val presensis = presensiService.findListEmployeeByIdEmployee(idEmployee)!!
        val presensiResponse = presensis.map {
            convertToPresensiResponse(it)
        }

        return ResponseEntity(presensiResponse, HttpStatus.OK)
    }

    @GetMapping("/presensi/all")
    @PreAuthorize("hasAnyAuthority('user:read', 'user:create', 'user:update')")
    fun allPresensi(): ResponseEntity<List<PresensiResponse>> {
        val allPresensi = presensiService.findAllPresensi()
        val allPresensiResponse = allPresensi.map {
            convertToPresensiResponse(it)
        }

        return ResponseEntity(allPresensiResponse, HttpStatus.OK)
    }

    private fun convertToPresensiResponse(presensi: Presensi): PresensiResponse {
        return PresensiResponse(
            employee = presensi.employee,
            date = presensi.date,
            timeIn = presensi.timeIn,
            timeOut = presensi.timeOut,
            note = presensi.note,
            locationIn = presensi.locationIn,
            locationOut = presensi.locationOut
        )
    }
}