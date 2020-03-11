package com.elemental.emailapi.service;

import com.elemental.emailapi.dto.ContactFormDto;
import com.elemental.emailapi.factories.MailServerPropsFactory;
import com.elemental.emailapi.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final Logger logger = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    public ResponseEntity<ContactFormDto> sendContactFormNotification(ContactFormDto contactFormDto) {
        ResponseEntity<ContactFormDto> responseEntity;
        final String username = System.getProperty("elemental.email.user");
        final String password = System.getProperty("elemental.email.password");
        Properties mailServerProperties = MailServerPropsFactory.createEmailPropsForElementalAPIGmail();

        logger.info("Sending Frankie an email from " + contactFormDto.getName());
        logger.info("Email of sender: " + contactFormDto.getEmail());
        logger.info("Message: " + contactFormDto.getMessage());


        try {
            this.sendEmailToElementalGmailSMTP(mailServerProperties, username, password, contactFormDto);

            responseEntity = new ResponseEntity<>(contactFormDto, HttpStatus.OK);
        } catch (Exception exception) {
            contactFormDto.setException(exception);
            contactFormDto.setExceptionNote("Exception Occurred in sendContactFormNotification: " + exception);
            logger.error(contactFormDto.getExceptionNote());
            responseEntity = new ResponseEntity<>(contactFormDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    public void sendEmailToElementalGmailSMTP(Properties mailServerProperties, String username, String password, ContactFormDto contactFormDto) throws MessagingException {
        Session getMailSession = Session.getInstance(mailServerProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        MimeMessage message = new MimeMessage(getMailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(Constants.FRANKIE_EMAIL));
        message.setSubject("Email Contact Notification: " + contactFormDto.getAppName());
        message.setContent(String.format("email: %s \n\n name: %s \n\n message: %s", contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getMessage()), MediaType.TEXT_HTML_VALUE);
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect(Constants.GMAIL_SMTP_TYPE, username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
