package com.elemental.emailapi.service;

import com.elemental.emailapi.dto.ContactFormDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailNotificationService {

    private final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    public ResponseEntity<ContactFormDto> sendContactFormNotification(ContactFormDto contactFormDto) {
        ResponseEntity<ContactFormDto> responseEntity;
        final String username = "elementalemailapi@gmail.com";
        final String password = "Emailapi1497!";
        Transport transport;
        Properties mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");


        try{
            Session getMailSession = Session.getInstance(mailServerProperties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            MimeMessage message = new MimeMessage(getMailSession);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("rfrankie04@gmail.com"));
            message.setSubject("Email Contact Notification: " + contactFormDto.getAppName());
            message.setContent(String.format("email: %s \n\n name: %s \n\n message: %s", contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getMessage()), "text/html");
            transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            responseEntity = new ResponseEntity<>(contactFormDto, HttpStatus.OK);
        } catch (Exception exception){
            contactFormDto.setError("Exception Occurred in sendContactFormNotification: " + exception);
            logger.error(contactFormDto.getError());
            responseEntity = new ResponseEntity<>(contactFormDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
