package com.example.userpestcontrol.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import java.util.*

data class HttpResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm z", timezone = "Asia/Jakarta")
    @JsonProperty("timestamp")
    val timestamp: Date = Date(),
    val httpStatusCode: Int,
    val httpStatus: HttpStatus,
    val reason: String,
    val message: String,
)