package com.example.userpestcontrol.service.department

import com.example.userpestcontrol.entity.Department
import com.example.userpestcontrol.exception.domain.DepartmentExistException
import com.example.userpestcontrol.repository.DepartmentRepository
import com.example.userpestcontrol.repository.EmployeeRepository
import com.mualim.syahrido.userpestcontrol.logger.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Qualifier("userDetails")
class DepartmentServiceImp @Autowired constructor(
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
) : DepartmentService {

    override fun saveDepartment(department: Department): Department {
        val currentDepartment = departmentRepository.findByName(department.name)

        if (currentDepartment?.name != null) {
            throw DepartmentExistException("Department already exist")
        }

        log.info("Save a new department")
        return departmentRepository.save(department)
    }

    override fun addDepartmentToEmployee(idEmployee: Long, departmentName: String) {
        val employee = employeeRepository.findUserByIdEmployee(idEmployee)
        val department = departmentRepository.findByName(departmentName)

        log.info("Added area $departmentName to employee with id $idEmployee")
        if (employee != null) {
            employee.department = department
        }
    }

    companion object {
        private val log = Logger.log
    }
}