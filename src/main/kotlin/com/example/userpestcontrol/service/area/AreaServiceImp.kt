package com.example.userpestcontrol.service.area

import com.example.userpestcontrol.entity.Area
import com.example.userpestcontrol.exception.domain.AreaExistException
import com.example.userpestcontrol.repository.AreaRepository
import com.example.userpestcontrol.repository.EmployeeRepository
import com.mualim.syahrido.userpestcontrol.logger.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Qualifier("userDetails")
class AreaServiceImp(
    private val areaRepository: AreaRepository,
    private val employeeRepository: EmployeeRepository,
) : AreaService {

    override fun saveArea(area: Area): Area {
        val currentArea = areaRepository.findByName(area.name)

        if (currentArea?.name != null) {
            throw AreaExistException("Area already exist")
        }

        log.info("Save a new area")
        return areaRepository.save(area)
    }

    override fun addAreaToEmployee(idEmployee: Long, areaName: String) {
        val employee = employeeRepository.findUserByIdEmployee(idEmployee)
        val area = areaRepository.findByName(areaName)

        log.info("Added area $areaName to employee with id $idEmployee")
        employee?.area = area
    }

    companion object {
        private val log = Logger.log
    }
}