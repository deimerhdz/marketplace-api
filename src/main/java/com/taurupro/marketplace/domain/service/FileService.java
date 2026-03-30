package com.taurupro.marketplace.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import java.text.Normalizer;
import java.time.Duration;

@Service
public class FileService {

    @Value("${aws.bucket}")
    private String bucketName;


    private final S3Presigner s3Presigner;

    private final S3Client s3Client;
    public FileService(S3Presigner s3Presigner,S3Client s3Client) {
        this.s3Client=  s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * Genera una URL pre-firmada para operaciones GET o PUT con el tipo de acceso especificado.
     */
    public String generatePreSignedUrl(String filePath,String fileName ,String contentType ,SdkHttpMethod method) {

        return generatePutPresignedUrl(filePath+buildFilename(fileName),contentType);

    }

    public void deleteFile(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
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
