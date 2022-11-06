package com.example.userpestcontrol.controller

import com.example.userpestcontrol.entity.Department
import com.example.userpestcontrol.model.form.DepartmentToEmployeeForm
import com.example.userpestcontrol.exception.ExceptionHandling
import com.example.userpestcontrol.service.department.DepartmentService
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
@RequestMapping("/employee")
class DepartmentController @Autowired constructor(
    private val departmentService: DepartmentService,
) : ExceptionHandling() {

    @PostMapping("/department/save")
    fun saveDepartment(@RequestBody department: Department): ResponseEntity<Department> {
        val uri =
            URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/employee/department/save")
                .toString())

        return ResponseEntity.created(uri).body(departmentService.saveDepartment(department))
    }

    @PostMapping("/department/add-department-to-employee")
    fun addDepartmentToEmployee(@RequestBody form: DepartmentToEmployeeForm): ResponseEntity<*> {
        departmentService.addDepartmentToEmployee(form.idEmployee, form.departmentName)

        return createHttpResponse(HttpStatus.OK,
            "Successfully added ${form.departmentName} to employee with id ${form.idEmployee}")
    }
}