package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.controller.dto.request.LoginRequest;
import com.github.farzan6118.petclinic.infrastructure.keycloak.LoginService;
import com.github.farzan6118.petclinic.infrastructure.keycloak.UserIdentityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class AuthController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        AccessTokenResponse response = loginService.getAccessToken(request.username(), request.password());

        return ResponseEntity.ok(response);
    }

}
