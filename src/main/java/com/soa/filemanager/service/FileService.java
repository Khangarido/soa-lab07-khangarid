package com.soa.filemanager.service;

import com.amazonaws.services.s3.AmazonS3;
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

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private AmazonS3 spacesClient;

    @Value("${spaces.bucket-name}")
    private String bucketName;

    @Value("${spaces.region}")
    private String region;

    public UploadResponse uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        log.info("Uploading file: {}", fileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest request = new PutObjectRequest(
            bucketName,
            fileName,
            file.getInputStream(),
            metadata
        );

        spacesClient.putObject(request);

        String publicUrl = String.format(
            "https://%s.%s.digitaloceanspaces.com/%s",
            bucketName, region, fileName
        );

        log.info("File uploaded successfully: {}", publicUrl);

        return new UploadResponse(publicUrl, fileName, true, "Upload successful");
    }
            }
