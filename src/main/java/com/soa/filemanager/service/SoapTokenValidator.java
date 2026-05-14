package com.soa.filemanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// SOAP сервисээр токен шалгах сервис
// File Manager руу хүсэлт ирэхэд эхлээд энэ классаар токен баталгаажуулна
@Service
public class SoapTokenValidator {

    private static final Logger log = LoggerFactory.getLogger(SoapTokenValidator.class);

    @Value("${soap.service.url}")
    private String soapServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // SOAP ValidateToken операц дуудна
    // Хариу: true = токен зөв, false = токен буруу эсвэл хугацаа дууссан
    public boolean validateToken(String token) {
        try {
            // SOAP XML хүсэлт бүтээнэ
            String soapRequest = buildValidateTokenRequest(token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            headers.set("SOAPAction", "ValidateToken");

            HttpEntity<String> entity = new HttpEntity<>(soapRequest, headers);

            // SOAP сервис рүү хүсэлт явуулна
            ResponseEntity<String> response = restTemplate.postForEntity(
                    soapServiceUrl, entity, String.class
            );

            // SOAP хариунаас true/false уншина
            String body = response.getBody();
            boolean isValid = body != null && body.contains("<return>true</return>");

            log.info("Токен шалгалт: {}", isValid ? "ЗӨВ" : "БУРУУ");
            return isValid;

        } catch (Exception e) {
            // SOAP сервис хүрэхгүй байвал access татгалзана
            log.error("SOAP токен шалгахад алдаа: {}", e.getMessage());
            return false;
        }
    }

    // ValidateToken SOAP XML хүсэлт бүтээх
    private String buildValidateTokenRequest(String token) {
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                  xmlns:user="http://service.soa.com/">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <user:ValidateToken>
                            <token>%s</token>
                        </user:ValidateToken>
                    </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(token);
    }
}
