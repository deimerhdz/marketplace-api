package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.service.BullService;
import com.taurupro.marketplace.domain.service.StrawService;
import com.taurupro.marketplace.persistence.model.UserMain;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/straws")
public class StrawController {
    private final StrawService strawService;

    public StrawController(StrawService strawService) {
        this.strawService = strawService;
    }

    @GetMapping("/{bullId}")
    public ResponseEntity<List<StrawDto>> getAll(@PathVariable UUID bullId) {
        return ResponseEntity.ok(this.strawService.list(bullId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<ResponseMessgeDto> create(@RequestBody @Valid CreateStrawDto createBullDto
    ) {
        this.strawService.save(createBullDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessgeDto("pajilla agregada con exito."));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<ResponseMessgeDto> update(@PathVariable UUID id,
                                                    @RequestBody @Valid UpdateStrawDto updateMovieDto
    ) {
        this.strawService.update(id, updateMovieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessgeDto("pajilla actualizada con exito."));
    }
}
