package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.constant.MessageConstant
import com.example.userpestcontrol.entity.Employee
import com.example.userpestcontrol.entity.EmployeePrincipal
import com.example.userpestcontrol.enum.Role
import com.example.userpestcontrol.exception.domain.EmailExistException
import com.example.userpestcontrol.exception.domain.EmailNotFoundException
import com.example.userpestcontrol.exception.domain.IdEmployeeExistException
import com.example.userpestcontrol.exception.domain.UserNotFoundException
import com.example.userpestcontrol.model.request.RegisterRequest
import com.example.userpestcontrol.repository.EmployeeRepository
import com.example.userpestcontrol.service.EmailService
import com.mualim.syahrido.userpestcontrol.logger.Logger
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@Qualifier("userDetailsService")
class EmployeeServiceImp @Autowired constructor(
    private val employeeRepository: EmployeeRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val emailService: EmailService
) : EmployeeService, UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val employee = employeeRepository.findUserByEmail(username)
        if (employee == null) {
            log.error(MessageConstant.NO_USER_FOUND_BY_EMAIL.plus(username))
            throw UsernameNotFoundException(MessageConstant.NO_USER_FOUND_BY_EMAIL.plus(username))
        } else {
//            validateLoginAttempts(employee)
            employee.run {
                lastLoginDisplayDate = employee.lastLoginDate
                lastLoginDate = Date().time
            }

            employeeRepository.save(employee)
            val employeePrincipal = EmployeePrincipal(employee)
            log.info(MessageConstant.FOUND_USER_BY_EMAIL + employeePrincipal.username)
            return employeePrincipal
        }
    }

    override fun register(registerRequest: RegisterRequest): Employee? {
        validateNewUsernameAndEmail(StringUtils.EMPTY, registerRequest.email, registerRequest.idEmployee)
        val employee = Employee(
            idEmployee = registerRequest.idEmployee,
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = encodedPassword(registerRequest.password),
            activeDate = Date().time,
            role = Role.ROLE_USER.name,
            authorities = Role.ROLE_USER.getAuthorities(),
            profileImageUrl = null
        )

        employeeRepository.save(employee)
        log.info("created a new employee")

        return employee
    }

    override fun findUserByEmail(email: String?): Employee? {
        return employeeRepository.findUserByEmail(email)
    }

    override fun findUserByIdEmployee(idEmployee: Long): Employee? {
        return employeeRepository.findUserByIdEmployee(idEmployee)
    }

    override fun getEmployees(): List<Employee>? {
        return employeeRepository.findAll()
    }

    override fun sendLink(email: String?) {
        val employee =
            findUserByEmail(email) ?: throw EmailNotFoundException("${MessageConstant.NO_USER_FOUND_BY_EMAIL} $email")

        emailService.sendToEmployeeEmail(employee.firstName, email)
    }

    override fun resetPassword(email: String?, newPassword: String?) {
        val employee =
            findUserByEmail(email) ?: throw EmailNotFoundException("${MessageConstant.NO_USER_FOUND_BY_EMAIL} $email")

        employee.password = encodedPassword(newPassword)
        employeeRepository.save(employee)
    }

    private fun encodedPassword(password: String?): String {
        return bCryptPasswordEncoder.encode(password)
    }

    private fun validateNewUsernameAndEmail(
        currentEmail: String?,
        newEmail: String?,
        idEmployee: Long,
    ): Employee? {
        val userByNewEmail = findUserByEmail(newEmail)
        val userByIdEmployee = findUserByIdEmployee(idEmployee)
        if (StringUtils.isNotBlank(currentEmail)) {
            val currentUser = findUserByEmail(currentEmail)
                ?: throw UserNotFoundException(MessageConstant.NO_USER_FOUND_BY_EMAIL + currentEmail)
            if (userByNewEmail != null && currentUser.id != userByNewEmail.id) {
                throw EmailExistException(MessageConstant.EMAIL_ALREADY_EXISTS)
            }
            if (userByIdEmployee != null && currentUser.id != userByIdEmployee.id) {
                throw IdEmployeeExistException(MessageConstant.ID_EMPLOYEE_ALREADY_EXISTS)
            }
            return currentUser
        } else {
            if (userByNewEmail != null) {
                throw EmailExistException(MessageConstant.EMAIL_ALREADY_EXISTS)
            }

            if (userByIdEmployee != null) {
                throw IdEmployeeExistException(MessageConstant.ID_EMPLOYEE_ALREADY_EXISTS)
            }
        }
        return null
    }

    companion object {
        private val log = Logger.log
    }
}
