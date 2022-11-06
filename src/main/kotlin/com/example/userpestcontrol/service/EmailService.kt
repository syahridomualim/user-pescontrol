package com.example.userpestcontrol.service

import com.example.userpestcontrol.constant.EmailConstant
import com.sun.mail.smtp.SMTPTransport
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

interface EmailService {

    /*
    * this method to send link to employee
    *
     */
    fun sendToEmployeeEmail(firstName: String?, email: String?)

}