package com.example.userpestcontrol.service.area

import com.example.userpestcontrol.entity.Area

interface AreaService {

    fun saveArea(area: Area): Area

    fun addAreaToEmployee(idEmployee: Long, areaName: String)
}