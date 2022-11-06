package com.example.userpestcontrol.filter

import com.example.userpestcontrol.constant.SecurityConstant
import com.example.userpestcontrol.model.response.HttpResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?,
    ) {
        val httpResponse = HttpResponse(
            httpStatusCode = UNAUTHORIZED.value(),
            httpStatus = UNAUTHORIZED,
            reason = UNAUTHORIZED.reasonPhrase.uppercase(),
            message = SecurityConstant.ACCESS_DENIED_MESSAGES
        )

        response?.run {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = UNAUTHORIZED.value()
        }

        val outputStream = response?.outputStream
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(outputStream, httpResponse)
        outputStream?.flush()
    }
}