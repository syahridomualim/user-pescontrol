package com.example.userpestcontrol.controller

import com.example.userpestcontrol.entity.Area
import com.example.userpestcontrol.model.form.AreaToEmployeeForm
import com.example.userpestcontrol.service.area.AreaService
import com.example.userpestcontrol.utility.createHttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping(value = ["/employee"])
class AreaController @Autowired constructor(
    private val areaService: AreaService,
) {

    @PostMapping("/area/save")
    fun saveArea(@RequestBody area: Area): ResponseEntity<Area> {
        val uri =
            URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/employee/area/save").toString())

        return ResponseEntity.created(uri).body(areaService.saveArea(area))
    }

    @PostMapping("area/add-to-area")
    fun addAreaToEmployee(@RequestBody form: AreaToEmployeeForm): ResponseEntity<*> {
        areaService.addAreaToEmployee(form.idEmployee, form.areaName)
        return createHttpResponse(HttpStatus.OK,
            "Successfully added ${form.areaName} to employee with id ${form.idEmployee}")
    }

}