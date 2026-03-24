package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.BreedDto;
import com.taurupro.marketplace.domain.service.BreedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/breeds")
public class BreedController {
    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public ResponseEntity<List<BreedDto>> getAll(){
        return ResponseEntity.ok(this.breedService.getAll());
    }
}
