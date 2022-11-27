package com.example.userpestcontrol.controller

import com.example.userpestcontrol.constant.SecurityConstant.JWT_TOKEN_HEADER
import com.example.userpestcontrol.entity.Employee
import com.example.userpestcontrol.entity.EmployeePrincipal
import com.example.userpestcontrol.exception.ExceptionHandling
import com.example.userpestcontrol.model.request.CoordinatorRegisterRequest
import com.example.userpestcontrol.model.request.EmployeeRegisterRequest
import com.example.userpestcontrol.model.request.LoginRequest
import com.example.userpestcontrol.model.response.EmployeeResponse
import com.example.userpestcontrol.model.response.HttpResponse
import com.example.userpestcontrol.model.response.LoginResponse
import com.example.userpestcontrol.model.response.RegisterResponse
import com.example.userpestcontrol.service.employee.EmployeeService
import com.example.userpestcontrol.utility.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/", "/employee"])
class EmployeeController @Autowired constructor(
    private val employeeService: EmployeeService,
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: TokenProvider,
) : ExceptionHandling() {

    /*
    * author: mualim
    * date: 13/11/2022
    * this method is invoked to register new employee
    * */
    @PostMapping("/register-employee")
    fun registerEmployee(@RequestBody employeeRegisterRequest: EmployeeRegisterRequest): ResponseEntity<RegisterResponse> {
        val newEmployee = employeeService.registerEmployee(employeeRegisterRequest)

        return ResponseEntity(convertToRegisterResponse(newEmployee), OK)
    }

    /*
    * author: mualim
    * date: 13/11/2022
    * this method is invoked to register new coordinator
    * */
    @PostMapping("/register-coordinator")
    fun registerCoordinator(@RequestBody coordinatorRegisterRequest: CoordinatorRegisterRequest): ResponseEntity<RegisterResponse> {
        val newCoordinator = employeeService.registerCoordinator(coordinatorRegisterRequest)

        return ResponseEntity(convertToRegisterResponse(newCoordinator), OK)
    }

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
                token = tokenProvider.generateJwtToken(employeePrincipal)
            )
        }

        return ResponseEntity(loginResponse, jwtHeader, OK)
    }

    /*
    * author mualim
    * invoke all employees
    * */
    @GetMapping("/employees")
    fun getEmployees(): ResponseEntity<List<EmployeeResponse>> {
        val employees = employeeService.getEmployees()

        val employeesResponse = employees?.map {
            convertToEmployeeResponse(it)
        }

        return ResponseEntity(employeesResponse, OK)
    }

    @GetMapping("/{idEmployee}")
    fun getEmployee(@PathVariable idEmployee: Long): ResponseEntity<EmployeeResponse> {
        val employee = employeeService.findUserByIdEmployee(idEmployee)!!

        val employeeResponse = convertToEmployeeResponse(employee)
        return ResponseEntity(employeeResponse, OK)
    }

    /*
    * send link to email's employee
    * */
    @GetMapping("/send-link")
    fun sendLink(@RequestParam("email") email: String?): ResponseEntity<HttpResponse> {
        employeeService.sendLink(email)
        return OK.response("Please check email to reset your password")
    }

    /*
    * set new password
    * */
    @GetMapping("/reset-password")
    fun resetPassword(
        @RequestParam("email") email: String?,
        @RequestParam("reset-password") newPassword: String?
    ): ResponseEntity<HttpResponse> {
        employeeService.resetPassword(email, newPassword)
        return OK.response("password has been changed")
    }


    private fun authenticate(email: String, password: String) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
    }

    private fun getJwtHeader(employeePrincipal: EmployeePrincipal): HttpHeaders {
        return HttpHeaders().also {
            it.add(JWT_TOKEN_HEADER, tokenProvider.generateJwtToken(employeePrincipal))
        }
    }

    private fun convertToEmployeeResponse(employee: Employee): EmployeeResponse {
        return EmployeeResponse(
            idEmployee = employee.idEmployee,
            name = "${employee.firstName!!} ${employee.lastName!!}",
            email = employee.email!!,
            profileImageUrl = employee.profileImageUrl,
            activeDate = employee.activeDate,
            role = employee.role,
            authorities = employee.authorities,
            lastLoginDate = employee.lastLoginDate,
            lastLoginDisplayDate = employee.lastLoginDisplayDate,
            isNotLocked = employee.isNotLocked,
            isActive = employee.isActive,
            area = employee.area,
            department = employee.department
        )
    }

    private fun HttpStatus.response(message: String): ResponseEntity<HttpResponse> {
        val body = HttpResponse(
            httpStatus = OK,
            httpStatusCode = OK.value(),
            reason = OK.reasonPhrase,
            message = message
        )
        return ResponseEntity(body, this)
    }

    private fun convertToRegisterResponse(employee: Employee?): RegisterResponse {
        return RegisterResponse(
            idEmployee = employee?.idEmployee,
            name = "${employee?.firstName} ${employee?.lastName}",
            email = employee?.email,
            activeDate = employee?.activeDate,
            role = employee?.role
        )
    }
}