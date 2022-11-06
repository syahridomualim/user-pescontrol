package com.example.userpestcontrol.model.response

import com.example.userpestcontrol.entity.Employee

data class PresensiResponse(
    val employee: Employee,

    val date: Long?,

    var timeIn: Long?,

    var timeOut: Long?,

    var note: String?,

    var locationIn: String?,

    var locationOut: String?,
)