package com.elemental.emailapi.factories;

import java.util.Properties;

public class MailServerPropsFactory {
    public static Properties createEmailPropsForElementalAPIGmail() {
        Properties mailServerProperties = System.getProperties();

        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        return mailServerProperties;
    }
}
