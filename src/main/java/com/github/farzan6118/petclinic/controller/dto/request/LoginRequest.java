package com.github.farzan6118.petclinic.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "username is required")
        @Schema(example = "user-one@test.com", format = "email")
        String username,

        @NotBlank(message = "password is required")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Schema(example = "test", format = "password")
        String password

) {
}

