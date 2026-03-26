package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.http.SdkHttpMethod;
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
            @RequestParam(name = "filePath", required = false, defaultValue = "") String filePath,
            @RequestParam(name = "filename", required = false, defaultValue = "") String filename,
            @RequestParam(name = "contentType", required = false, defaultValue = "") String contentType) {

        String url = fileService.generatePreSignedUrl(filePath,filename,contentType, SdkHttpMethod.PUT );
        return ResponseEntity.ok(Map.of("url", url, "file", filename));
    }
}
