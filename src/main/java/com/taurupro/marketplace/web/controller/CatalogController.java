package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.CatalogDetailDto;
import com.taurupro.marketplace.domain.dto.CatalogDto;
import com.taurupro.marketplace.domain.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService bullService) {
        this.catalogService = bullService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogDto>> getAll(@RequestParam(defaultValue = "") String name){
        return ResponseEntity.ok(this.catalogService.getAll(name));
    }
    @GetMapping("/{slug}")
    public ResponseEntity<CatalogDetailDto> getDetailBySlug(@PathVariable() String slug){
        return ResponseEntity.ok(this.catalogService.getDetailBySlug(slug));
    }
    @GetMapping("/recent")
    public ResponseEntity<List<CatalogDto>> getBullRecents(@RequestParam(defaultValue = "10") Integer limit){
        return ResponseEntity.ok(this.catalogService.getRecentBulls(limit));
    }
}
