package com.github.farzan6118.petclinic.infrastructure.keycloak;

import org.keycloak.representations.AccessTokenResponse;

public interface RefreshTokenService {
    AccessTokenResponse refreshToken(String refreshToken);
}
