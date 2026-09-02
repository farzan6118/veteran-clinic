package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.dto.request.CreateWeeklyAvailabilityRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateWeeklyAvailabilityRequestDto;

import java.util.UUID;

public interface VetScheduleService {

    void createWeeklyAvailability(UUID vetUuid, CreateWeeklyAvailabilityRequestDto request);

    void updateWeeklyAvailability(UUID vetUuid, Long availabilityId,
                                  UpdateWeeklyAvailabilityRequestDto request);

    void deleteWeeklyAvailability(UUID vetUuid, Long availabilityId);
}
