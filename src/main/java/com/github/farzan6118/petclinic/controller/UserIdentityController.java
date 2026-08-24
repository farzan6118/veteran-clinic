package com.github.farzan6118.petclinic.controller;

import com.github.farzan6118.petclinic.controller.dto.response.UserInfoResponseDto;
import com.github.farzan6118.petclinic.infrastructure.keycloak.UserIdentityService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserIdentityController {

    private final UserIdentityService userIdentityService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userIdentityService.getCurrentUserInfo(jwt)
        );
    }
}
