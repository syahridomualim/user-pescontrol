package com.example.userpestcontrol.utility

import com.example.userpestcontrol.model.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun createHttpResponse(httpStatus: HttpStatus, message: String?): ResponseEntity<HttpResponse> {
    val httpResponse = HttpResponse(
        httpStatusCode = httpStatus.value(),
        httpStatus = httpStatus,
        reason = httpStatus.reasonPhrase.uppercase(),
        message = message!!
    )

    return ResponseEntity<HttpResponse>(httpResponse, httpStatus)
}