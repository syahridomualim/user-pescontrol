package com.example.userpestcontrol.service.email

import com.example.userpestcontrol.constant.EmailConstant
import com.example.userpestcontrol.service.EmailService
import com.sun.mail.smtp.SMTPTransport
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class EmailServiceImpl : EmailService {

    override fun sendToEmployeeEmail(firstName: String?, email: String?) {
        val message: Message = createMessage(firstName, email)

        val smtpTransport: SMTPTransport =
            getEmailSession()?.getTransport(EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL) as SMTPTransport

        smtpTransport.apply {
            connect(EmailConstant.GMAIL_SMTP_SERVER, EmailConstant.USERNAME, EmailConstant.PASSWORD)
            sendMessage(message, message.allRecipients)
            close()
        }
    }


    private fun createMessage(firstName: String?, email: String?): Message {
        val message: Message = MimeMessage(getEmailSession())

        // send to employee
        message.apply {
            setFrom(InternetAddress(EmailConstant.FROM_EMAIL))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
            setRecipients(Message.RecipientType.CC, InternetAddress.parse(EmailConstant.CC_EMAIL, false))
            subject = EmailConstant.EMAIL_SUBJECT
            setText("Hello $firstName, \n\n Please click below url to reset your password:\n\n http://localhost:4200/reset-password The Support Team")
            sentDate = Date()
            saveChanges()
        }

        return message
    }

    private fun getEmailSession(): Session? {
        val properties = System.getProperties()

        properties.apply {
            put(EmailConstant.SMTP_HOST, EmailConstant.GMAIL_SMTP_SERVER)
            put(EmailConstant.SMTP_AUTH, true)
            put(EmailConstant.SMTP_PORT, EmailConstant.DEFAULT_PORT)
            put(EmailConstant.SMTP_STARTTLS_ENABLE, true)
            put(EmailConstant.SMTP_STARTTLS_REQUIRED, true)
        }

        return Session.getInstance(properties)
    }
}