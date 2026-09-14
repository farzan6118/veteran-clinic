package com.github.farzan6118.petclinic.infrastructure.keycloak.client;

import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakTokenClient {
    AccessTokenResponse getAccessToken(
            String username,
            String password
    );
}
