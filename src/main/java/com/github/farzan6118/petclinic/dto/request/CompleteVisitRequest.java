package com.github.farzan6118.petclinic.dto.request;

public record CompleteVisitRequest(
        String diagnosis,
        String notes

) {
}
