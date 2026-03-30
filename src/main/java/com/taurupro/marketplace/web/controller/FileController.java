package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.ResourceDto;
import com.taurupro.marketplace.domain.dto.PreSignedUrlsResponse;
import com.taurupro.marketplace.domain.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.http.SdkHttpMethod;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * Generates a presigned PUT URL with specified access type.
     */
    @PostMapping("/pre-signed-url")
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> generateUrl(
            @RequestParam(name = "filePath", required = true, defaultValue = "") String filePath,
            @RequestParam(name = "filename", required = true, defaultValue = "") String filename,
            @RequestParam(name = "contentType", required = true, defaultValue = "") String contentType) {

        String url = fileService.generatePreSignedUrl(filePath,filename,contentType, SdkHttpMethod.PUT );
        return ResponseEntity.ok(Map.of("url", url, "file", filename));
    }
    @PostMapping("/pre-signed-urls")
    @PreAuthorize("hasAnyAuthority('SUPPLIER')")
    public ResponseEntity<PreSignedUrlsResponse> generateUrls(
            @RequestParam(name = "filePath", required = true, defaultValue = "") String filePath,
            @RequestBody @Valid ResourceDto galleryDto) {
        List<String> urls = galleryDto.files().stream()
                .map(file -> fileService.generatePreSignedUrl(
                        filePath, file.key(), file.contentType(), SdkHttpMethod.PUT))
                .toList();
       ;
        return ResponseEntity.ok(new PreSignedUrlsResponse(urls));
    }
}
