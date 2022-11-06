package com.example.userpestcontrol.filter

import com.example.userpestcontrol.constant.SecurityConstant
import com.example.userpestcontrol.model.response.HttpResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint : Http403ForbiddenEntryPoint() {

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        exception: AuthenticationException?,
    ) {
        val httpResponse = HttpResponse(
            httpStatusCode = FORBIDDEN.value(),
            httpStatus = FORBIDDEN,
            reason = FORBIDDEN.reasonPhrase.uppercase(),
            message = SecurityConstant.FORBIDDEN_MESSAGES
        )

        response.run {
            contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = FORBIDDEN.value()
        }

        val outputStream = response.outputStream
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(outputStream, httpResponse)
        outputStream.flush()
    }
}