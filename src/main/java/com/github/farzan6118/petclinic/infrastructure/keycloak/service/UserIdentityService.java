package com.github.farzan6118.petclinic.infrastructure.keycloak.service;

import com.github.farzan6118.petclinic.auth.dto.response.UserInfoResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserIdentityService {
    UserInfoResponseDto getCurrentUserInfo(Jwt jwt);
}
