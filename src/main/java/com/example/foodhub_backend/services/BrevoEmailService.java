package com.example.foodhub_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import brevo.ApiClient;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;

@Service
public class BrevoEmailService {

    private final TransactionalEmailsApi emailApi;

    public BrevoEmailService(@Value("${brevo.api.key}") String apiKey){

        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(apiKey);

        this.emailApi = new TransactionalEmailsApi(apiClient);

    }

    public void sendEmail(String to, String subject, String htmlContent){

        SendSmtpEmail email = new SendSmtpEmail();

        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail("lutadesahil@gmail.com");
        sender.setName("FoodHub");

        email.setSender(sender);

        SendSmtpEmailTo recipient = new SendSmtpEmailTo();
        recipient.setEmail(to);
        email.setTo(java.util.Collections.singletonList(recipient));

        email.setSubject(subject);
        email.setHtmlContent(htmlContent);

        try{
            CreateSmtpEmail response = emailApi.sendTransacEmail(email);
            System.out.println("Email Sent! ID: " + response.getMessageId());
        }
        catch(Exception e){
            throw new RuntimeException("Brevo SDK email failed: " + e.getMessage(), e);
        }

    }

}
