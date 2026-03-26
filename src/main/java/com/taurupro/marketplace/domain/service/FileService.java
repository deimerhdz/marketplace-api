package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.enums.AccessType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.Duration;

@Service
public class FileService {

    @Value("${aws.bucket}")
    private String bucketName;


    private final S3Presigner s3Presigner;


    public FileService(S3Presigner s3Presigner) {

        this.s3Presigner = s3Presigner;
    }

    /**
     * Genera una URL pre-firmada para operaciones GET o PUT con el tipo de acceso especificado.
     */
    public String generatePreSignedUrl(String filePath,String fileName ,String contentType ,SdkHttpMethod method) {

        System.out.println("KEY FINAL: " + filePath + buildFilename(fileName));
        return generatePutPresignedUrl(filePath+buildFilename(fileName),contentType);

    }

    /**
     * Generates a presigned PUT URL with optional ACL based on AccessType.
     */
    private String generatePutPresignedUrl(String filePath,String contentType) {
        PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .contentType(contentType);


        PutObjectRequest putObjectRequest = putObjectRequestBuilder.build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }


    /**
     * Builds a sanitized filename with a timestamp prefix.
     */
    public static String buildFilename(String filename) {
        return String.format("%s_%s", System.currentTimeMillis(), sanitizeFileName(filename));
    }

    /**
     * Sanitizes the filename by removing special characters and replacing spaces.
     */
    private static String sanitizeFileName(String fileName) {
        String normalizedFileName = Normalizer.normalize(fileName, Normalizer.Form.NFKD);
        return normalizedFileName.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9.\\-_]", "");
    }
}
