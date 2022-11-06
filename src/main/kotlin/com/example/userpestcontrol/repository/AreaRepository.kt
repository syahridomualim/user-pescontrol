package com.example.userpestcontrol.repository

import com.example.userpestcontrol.entity.Area
import org.springframework.data.jpa.repository.JpaRepository

interface AreaRepository: JpaRepository<Area, Long>{

    fun findByName(name: String): Area?

}