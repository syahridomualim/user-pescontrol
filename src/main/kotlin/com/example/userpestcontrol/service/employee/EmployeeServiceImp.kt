package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.domain.Employee
import com.example.userpestcontrol.domain.EmployeePrincipal
import com.example.userpestcontrol.domain.request.RegisterRequest
import com.example.userpestcontrol.exception.domain.EmailExistException
import com.example.userpestcontrol.exception.domain.IdEmployeeExistException
import com.example.userpestcontrol.exception.domain.UserNotFoundException
import com.example.userpestcontrol.repository.EmployeeRepository
import com.mualim.syahrido.userpestcontrol.logger.Logger
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@Qualifier("userDetailsService")
class EmployeeServiceImp @Autowired constructor(
    private val employeeRepository: EmployeeRepository,
) : EmployeeService, UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val employee = employeeRepository.findUserByEmail(username)
        if (employee == null) {
            log.error(NO_USER_FOUND_BY_EMAIL.plus(username))
            throw UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL.plus(username))
        } else {
//            validateLoginAttempts(employee)
            employee.run {
                lastLoginDisplayDate = employee.lastLoginDate
                lastLoginDate = Date().time
            }

            employeeRepository.save(employee)
            val employeePrincipal = EmployeePrincipal(employee)
            log.info(FOUND_USER_BY_EMAIL + employeePrincipal.username)
            return employeePrincipal
        }
    }

    override fun register(registerRequest: RegisterRequest): Employee? {
        TODO("Not yet implemented")
    }

    override fun findUserByEmail(email: String?): Employee? {
        return employeeRepository.findUserByEmail(email)
    }

    override fun findUserByIdEmployee(idEmployee: Long): Employee? {
        return employeeRepository.findUserByIdEmployee(idEmployee)
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
                ?: throw UserNotFoundException(NO_USER_FOUND_BY_EMAIL + currentEmail)
            if (userByNewEmail != null && currentUser.id != userByNewEmail.id) {
                throw EmailExistException(EMAIL_ALREADY_EXISTS)
            }
            if (userByIdEmployee != null && currentUser.id != userByIdEmployee.id) {
                throw IdEmployeeExistException(ID_EMPLOYEE_ALREADY_EXISTS)
            }
            return currentUser
        } else {
            if (userByNewEmail != null) {
                throw EmailExistException(EMAIL_ALREADY_EXISTS)
            }

            if (userByIdEmployee != null) {
                throw IdEmployeeExistException(ID_EMPLOYEE_ALREADY_EXISTS)
            }
        }
        return null
    }

    companion object {
        private val log = Logger.log
        private const val EMAIL_ALREADY_EXISTS = "Email already exists"
        private const val ID_EMPLOYEE_ALREADY_EXISTS = "Id Employee already exists"
        private const val FOUND_USER_BY_EMAIL = "Returning found user by email: "
        private const val NO_USER_FOUND_BY_EMAIL = "No user found for email: "
    }
}
