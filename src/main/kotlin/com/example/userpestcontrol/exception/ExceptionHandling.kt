package com.example.userpestcontrol.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.userpestcontrol.exception.domain.*
import com.example.userpestcontrol.model.response.HttpResponse
import com.example.userpestcontrol.utility.createHttpResponse
import com.mualim.syahrido.userpestcontrol.logger.Logger
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.io.IOException
import java.util.*
import javax.persistence.NoResultException


@RestControllerAdvice
@EnableWebMvc
class ExceptionHandling : ErrorController {

    @ExceptionHandler(DisabledException::class)
    fun accountDisabledException(): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION)
    }

    @ExceptionHandler(LockedException::class)
    fun lockedException(): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun tokenExpiredException(exception: TokenExpiredException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.message)
    }

    @ExceptionHandler(EmailExistException::class)
    fun emailExistException(exception: EmailExistException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(IdEmployeeExistException::class)
    fun usernameExistException(exception: IdEmployeeExistException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(EmailNotFoundException::class)
    fun emailNotFoundException(exception: EmailNotFoundException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundException(exception: UserNotFoundException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(AreaExistException::class)
    fun areaExistException(exception: AreaExistException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(DepartmentExistException::class)
    fun departmentExistException(exception: DepartmentExistException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message)
    }

    @ExceptionHandler(CheckedInException::class)
    fun checkInException(exception: CheckedInException): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.OK, exception.message)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotSupportedException(exception: HttpRequestMethodNotSupportedException): ResponseEntity<HttpResponse> {
        val supportedMethod = Objects.requireNonNull(exception.supportedHttpMethods).iterator().next()
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod))
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorException(exception: Exception): ResponseEntity<HttpResponse> {
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG)
    }

    //    @ExceptionHandler(NotAnImageFileException.class)
    //    public ResponseEntity<HttpResponse> notAnImageFileException(NotAnImageFileException exception) {
    //        LOGGER.error(exception.getMessage());
    //        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    //    }

    @ExceptionHandler(NoResultException::class)
    fun notFoundException(exception: NoResultException): ResponseEntity<HttpResponse> {
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.message)
    }

    @ExceptionHandler(IOException::class)
    fun iOException(exception: IOException): ResponseEntity<HttpResponse> {
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(ex: NoHandlerFoundException?): ResponseEntity<HttpResponse> {
        return createHttpResponse(HttpStatus.NOT_FOUND, "There is no mapping for this URL")
    }

    companion object {
        private val LOGGER = Logger.log
        private const val ACCOUNT_LOCKED = "Your account has been locked. Please contact administration"
        private const val METHOD_IS_NOT_ALLOWED =
            "This request method is not allowed on this endpoint. Please send a '%s' request"
        private const val INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request"
        private const val INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again"
        private const val ACCOUNT_DISABLED =
            "Your account has been disabled. If this is an error, please contact administration"
        private const val ERROR_PROCESSING_FILE = "Error occurred while processing file"
        private const val NOT_ENOUGH_PERMISSION = "You do not have enough permission"
    }
}