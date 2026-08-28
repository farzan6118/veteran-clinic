package com.github.farzan6118.petclinic.infrastructure.keycloak;

import com.github.farzan6118.petclinic.dto.response.UserInfoResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserIdentityService {
    UserInfoResponseDto getCurrentUserInfo(Jwt jwt);
}
