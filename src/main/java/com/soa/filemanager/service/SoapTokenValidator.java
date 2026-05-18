package com.soa.filemanager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SoapTokenValidator {
    private static final Logger log = LoggerFactory.getLogger(SoapTokenValidator.class);

    private String soapServiceUrl = "https://soa-soap-service-d2ysa.ondigitalocean.app/ws";

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateToken(String token) {
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

    private String buildValidateTokenRequest(String token) {
        return String.format(
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:auth=\"http://example.com/auth\">" +
            "<soapenv:Body><auth:validateTokenRequest><auth:token>%s</auth:token></auth:validateTokenRequest></soapenv:Body>" +
            "</soapenv:Envelope>", token);
    }
        }
