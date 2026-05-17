package com.soa.filemanager.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class SoapTokenValidator {


    private static final Logger log = LoggerFactory.getLogger(SoapTokenValidator.class);


    @Value("${soap.service.url}")
    private String soapServiceUrl;


    private final RestTemplate restTemplate = new RestTemplate();


    public boolean validahteToken(String token) {
        try {
            String soapRequest = buildValidateTokenRequest(token);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(soapRequest, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(soapServiceUrl, entity, String.class);
            String body = response.getBody();
            boolean isValid = body != null && body.contains("valid>true</");
            log.info("Token check: {}", isValid ? "VALID" : "INVALID");
            return isValid;
        } catch (Exception e) {
            log.error("SOAP token validation error: {}", e.getMessage());
            return false;
        }
    }

