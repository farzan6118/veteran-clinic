package com.github.farzan6118.petclinic.infrastructure.keycloak.service;

import org.keycloak.representations.AccessTokenResponse;

public interface RefreshTokenService {
    AccessTokenResponse refreshToken(String refreshToken);
}
