package com.example.userpestcontrol.controller

import com.example.userpestcontrol.constant.SecurityConstant.JWT_TOKEN_HEADER
import com.example.userpestcontrol.domain.EmployeePrincipal
import com.example.userpestcontrol.domain.request.LoginRequest
import com.example.userpestcontrol.domain.response.LoginResponse
import com.example.userpestcontrol.exception.ExceptionHandling
import com.example.userpestcontrol.service.employee.EmployeeService
import com.example.userpestcontrol.utility.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/", "/employee"])
class EmployeeController @Autowired constructor(
    private val employeeService: EmployeeService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
) : ExceptionHandling() {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        authenticate(loginRequest.email, loginRequest.password)
        val employee = employeeService.findUserByEmail(loginRequest.email)
        val employeePrincipal = EmployeePrincipal(employee)
        val jwtHeader = getJwtHeader(employeePrincipal)

        val loginResponse = employee?.let {
            LoginResponse(
                id = employee.id!!,
                idEmployee = it.idEmployee,
                name = "${employee.firstName} ${employee.lastName}",
                token = jwtTokenProvider.generateJwtToken(employeePrincipal)
            )
        }

        return ResponseEntity(loginResponse, jwtHeader, HttpStatus.OK)
    }

    private fun authenticate(email: String, password: String) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
    }

    private fun getJwtHeader(employeePrincipal: EmployeePrincipal): HttpHeaders {
        return HttpHeaders().also {
            it.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(employeePrincipal))
        }
    }

    private fun getToken(userPrincipal: EmployeePrincipal): String {
        return jwtTokenProvider.generateJwtToken(userPrincipal)
    }

}