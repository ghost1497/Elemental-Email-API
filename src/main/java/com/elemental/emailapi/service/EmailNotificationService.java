package com.elemental.emailapi.service;

import com.elemental.emailapi.dto.ContactFormDto;
import org.springframework.http.ResponseEntity;

public interface EmailNotificationService {

    ResponseEntity<ContactFormDto> sendContactFormNotification(ContactFormDto contactFormDto);
}
