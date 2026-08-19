package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class AuthController {

    private final LoginService loginService;

    @Operation(tags = {"1-login"})
    @GetMapping
    public ResponseEntity<AccessTokenResponse> adminLogin(
            @Schema(defaultValue = "admin@test.com",
                    allowableValues = {
                            "admin@test.com",
                            "manager@test.com",
                            "user-one@test.com",
                            "user-two@test.com"
                    })
            @RequestParam String username) {
        AccessTokenResponse context = loginService.getToken(username, "test");
        return ResponseEntity.ok().body(context);
    }

}
