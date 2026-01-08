package com.example.foodDelivery.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BrevoEmailService {

	private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
	
	@Value("${brevo.api.key}")
	private String apiKey;
	
	public void sendEmail(String to, String subject, String textContent) {
		
		try {
			String payload = """
					{
							"sender": { "name": "FoodHub", "email": "no-reply@foodhub.com" },
							"to": [{ "email": "%s" }],
							"subject": "%s",
							"textContent": "%s"
					}
			""".formatted(to, subject, textContent);
			
			HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(BREVO_URL))
			.header("Content-Type", "application/json")
			.header("api-key", apiKey)
			.POST(HttpRequest.BodyPublishers.ofString(payload))
			.build();
			
			HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to send email via Brevo", e);
		}
		
	}
	
}
