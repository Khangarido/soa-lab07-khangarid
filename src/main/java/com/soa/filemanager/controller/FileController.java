package com.soa.filemanager.controller;

import com.soa.filemanager.model.UploadResponse;
import com.soa.filemanager.service.FileService;
import com.soa.filemanager.service.SoapTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Файл хуулах REST endpoint-ууд
@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private SoapTokenValidator tokenValidator;

    // POST /files/upload - Файл хуулах
    // Header: Authorization: Bearer <token>
    // Body: multipart/form-data, file талбар
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam("file") MultipartFile file) {

        // 1. Токен байгаа эсэхийг шалгана
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header байхгүй");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UploadResponse(null, null, false, "Токен шаардлагатай"));
        }

        // "Bearer " хэсгийг авч хаяна
        String token = authHeader.substring(7);

        // 2. SOAP сервисээр токен баталгаажуулна
        boolean isValid = tokenValidator.validateToken(token);
        if (!isValid) {
            log.warn("Токен буруу эсвэл хугацаа дууссан");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UploadResponse(null, null, false, "Токен хүчингүй"));
        }

        // 3. Файл хоосон биш эсэхийг шалгана
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(null, null, false, "Файл сонгоогүй байна"));
        }

        try {
            // 4. DO Spaces руу хуулна
            UploadResponse response = fileService.uploadFile(file);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Файл хуулахад алдаа: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse(null, null, false, "Сервер алдаа: " + e.getMessage()));
        }
    }

    // GET /files/health - Сервис ажиллаж байгааг шалгах
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("File Manager Service ажиллаж байна");
    }
}
