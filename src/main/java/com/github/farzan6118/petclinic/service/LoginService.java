package com.github.farzan6118.petclinic.service;

import org.keycloak.representations.AccessTokenResponse;

public interface LoginService {
    AccessTokenResponse getToken(String username, String password);
}
