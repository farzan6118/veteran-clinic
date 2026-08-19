package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.KeycloakPropertiesDto;
import com.github.farzan6118.petclinic.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final RestTemplate restTemplate;
    private final KeycloakPropertiesDto keycloakProperties;

    @Override
    public AccessTokenResponse getToken(String username, String password) {
        String url = keycloakProperties.getAuthServerUrl() +
                "/realms/" + keycloakProperties.getRealm() +
                "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("realm", keycloakProperties.getRealm());
        body.add("client_id", keycloakProperties.getClientId());
        body.add("client_secret", keycloakProperties.getClientSecret());
        body.add("username", username);
        body.add("password", "test");
        body.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, AccessTokenResponse.class);
    }
}