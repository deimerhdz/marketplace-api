package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.ResponseMessgeDto;
import com.taurupro.marketplace.domain.dto.SignUpUser;
import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "8") int elements
                                                ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.getAll(page, elements));
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseMessgeDto> register(@Valid @RequestBody SignUpUser request) {
        userService.signUpUser(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessgeDto("Se ha registrado el usuario con éxito"));
    }
}
