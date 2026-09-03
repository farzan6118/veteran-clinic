package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateVetAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetAvailabilityRequestDto;

import java.util.UUID;

public interface VetAvailabilityService {

    void createAvailability(UUID vetUuid, CreateVetAvailabilityRequestDto request);

    void updateAvailability(UUID vetUuid, UUID availabilityUuid, UpdateVetAvailabilityRequestDto request);

    void deleteAvailability(UUID vetUuid, UUID availabilityUuid);
}