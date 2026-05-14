package com.soa.filemanager.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.soa.filemanager.model.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// DO Spaces руу файл хуулах үндсэн сервис
@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private AmazonS3 spacesClient;

    // environment variable-аас bucket нэрийг авна
    @Value("${spaces.bucket-name}")
    private String bucketName;

    @Value("${spaces.region}")
    private String region;

    // Файлыг DO Spaces руу хуулах
    // Буцаах утга: UploadResponse (URL, fileName, success)
    public UploadResponse uploadFile(MultipartFile file) throws IOException {
        // Файлын нэрийг timestamp-тай болгож давхцахаас сэргийлнэ
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        log.info("Файл хуулж байна: {}", fileName);

        // Файлын мэтадата тохируулна
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // DO Spaces руу хуулж, нийтэд унших эрх өгнө (PublicRead)
        PutObjectRequest request = new PutObjectRequest(
                bucketName,
                fileName,
                file.getInputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead);

        spacesClient.putObject(request);

        // Нийтэд харагдах URL бүтээнэ
        String publicUrl = String.format(
                "https://%s.%s.digitaloceanspaces.com/%s",
                bucketName, region, fileName
        );

        log.info("Файл амжилттай хуулагдлаа: {}", publicUrl);

        return new UploadResponse(publicUrl, fileName, true, "Амжилттай хуулагдлаа");
    }
}
