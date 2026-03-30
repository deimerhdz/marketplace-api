package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.enums.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.taurupro.marketplace.domain.service.BullService;
import com.taurupro.marketplace.persistence.model.UserMain;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;

@RestController
@RequestMapping("/bulls")
public class BullController {
    private final BullService bullService;

    public BullController(BullService bullService) {
        this.bullService = bullService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<Page<BullDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "8") int elements,
                                                @AuthenticationPrincipal UserMain userMain) {
        UUID supplierId = userMain.getSupplierId();
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.bullService.getAll(page, elements, supplierId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<BullDto> getById(@PathVariable UUID id,
                                           @AuthenticationPrincipal UserMain userMain) {
        UUID supplierId = userMain.getSupplierId();
        BullDto bullDto = this.bullService.getById(id, supplierId);

        if (bullDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bullDto);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<BullDto> create(@RequestBody @Valid CreateBullDto createBullDto,
                                          @AuthenticationPrincipal UserMain userMain) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.bullService.save(userMain.getSupplierId(), createBullDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BullDto> update(@PathVariable UUID id,
                                          @RequestBody @Valid UpdateBullDto updateMovieDto,
                                          @AuthenticationPrincipal UserMain userMain) {
        UUID supplierId = userMain.getSupplierId();
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bullService.update(id, supplierId, updateMovieDto));
    }

    @PutMapping("/update-resource/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<BullDto> updateVideo(@PathVariable UUID id,
                                               @RequestBody @Valid ResourceDto dto,
                                               @RequestParam(name = "resource", required = true, defaultValue = "") Resource resource,
                                               @AuthenticationPrincipal UserMain userMain) {
        UUID supplierId = userMain.getSupplierId();
        BullDto updated = bullService.updateResource(id, supplierId, dto.files(),resource);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/resource/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER')")
    public ResponseEntity<BullDto> deleteResource(   @PathVariable UUID id,
                                                     @RequestParam(name = "resource", required = true, defaultValue = "") Resource resource,
                                                     @RequestParam(name = "key", required = true, defaultValue = "") String key,
                                                     @AuthenticationPrincipal UserMain userMain) {
        UUID supplierId = userMain.getSupplierId();
        BullDto updated = bullService.deleteResource(id, supplierId,resource, key);

        return ResponseEntity.ok(updated);
    }
}
