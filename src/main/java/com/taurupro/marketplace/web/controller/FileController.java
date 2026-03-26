package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.enums.AccessType;
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
            @RequestParam(name = "filename", required = false, defaultValue = "") String filename) {

        String url = fileService.generatePreSignedUrl(filename, SdkHttpMethod.PUT );
        return ResponseEntity.ok(Map.of("url", url, "file", filename));
    }
}
