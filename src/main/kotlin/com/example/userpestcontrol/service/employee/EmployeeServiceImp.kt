package com.example.userpestcontrol.service.employee

import com.example.userpestcontrol.constant.MessageConstant
import com.example.userpestcontrol.entity.Area
import com.example.userpestcontrol.entity.Department
import com.example.userpestcontrol.entity.Employee
import com.example.userpestcontrol.entity.EmployeePrincipal
import com.example.userpestcontrol.enum.Role
import com.example.userpestcontrol.exception.domain.EmailExistException
import com.example.userpestcontrol.exception.domain.EmailNotFoundException
import com.example.userpestcontrol.exception.domain.IdEmployeeExistException
import com.example.userpestcontrol.exception.domain.UserNotFoundException
import com.example.userpestcontrol.model.request.CoordinatorRegisterRequest
import com.example.userpestcontrol.model.request.EmployeeRegisterRequest
import com.example.userpestcontrol.repository.EmployeeRepository
import com.example.userpestcontrol.service.email.EmailService
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

    override fun registerEmployee(employeeRegisterRequest: EmployeeRegisterRequest): Employee? {
        validateNewUsernameAndEmail(StringUtils.EMPTY, employeeRegisterRequest.email, employeeRegisterRequest.idEmployee)
        val employee = Employee(
            idEmployee = employeeRegisterRequest.idEmployee,
            firstName = employeeRegisterRequest.firstName,
            lastName = employeeRegisterRequest.lastName,
            email = employeeRegisterRequest.email,
            password = encodedPassword(employeeRegisterRequest.password),
            activeDate = Date().time,
            role = Role.ROLE_USER.name,
            authorities = Role.ROLE_USER.getAuthorities(),
            profileImageUrl = null,
            department = Department(1, "Pest Control"),
            area = employeeRegisterRequest.area
        )

        employeeRepository.save(employee)
        log.info("created a new employee")

        return employee
    }

    override fun registerCoordinator(coordinatorRegisterRequest: CoordinatorRegisterRequest): Employee? {
        // validate email or id employee exist or doesnt
        validateNewUsernameAndEmail(StringUtils.EMPTY, coordinatorRegisterRequest.email, coordinatorRegisterRequest.idEmployee)
        val employee = Employee(
            idEmployee = coordinatorRegisterRequest.idEmployee,
            firstName = coordinatorRegisterRequest.firstName,
            lastName = coordinatorRegisterRequest.lastName,
            password = encodedPassword(coordinatorRegisterRequest.password),
            email = coordinatorRegisterRequest.email,
            activeDate = Date().time,
            role = Role.ROLE_COORDINATOR.name,
            authorities = Role.ROLE_COORDINATOR.getAuthorities(),
            profileImageUrl = null
        )

        employeeRepository.save(employee)
        log.info("created a new coordinator")

        return employee
    }

    override fun findUserByEmail(email: String?): Employee? {
        log.info("find user by $email")
        return employeeRepository.findUserByEmail(email)
    }

    override fun findUserByIdEmployee(idEmployee: Long): Employee? {
        log.info("find user by $idEmployee")
        return employeeRepository.findUserByIdEmployee(idEmployee)
    }

    override fun getEmployees(): List<Employee>? {
        log.info("find all employees")
        return employeeRepository.findAll()
    }

    override fun sendLink(email: String?) {
        val employee =
            findUserByEmail(email) ?: throw EmailNotFoundException("${MessageConstant.NO_USER_FOUND_BY_EMAIL} $email")

        log.info("send link reset password to email user")
        emailService.sendToEmployeeEmail(employee.firstName, email)
    }

    override fun resetPassword(email: String?, newPassword: String?) {
        val employee =
            findUserByEmail(email) ?: throw EmailNotFoundException("${MessageConstant.NO_USER_FOUND_BY_EMAIL} $email")

        employee.password = encodedPassword(newPassword)
        log.info("saved new password")
        employeeRepository.save(employee)
    }

    private fun encodedPassword(password: String?): String {
        return bCryptPasswordEncoder.encode(password)
    }

    /*
    * author: this methode to validate
    * */
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
