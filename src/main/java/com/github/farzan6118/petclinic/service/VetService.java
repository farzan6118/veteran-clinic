package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.response.VetResponse;

import java.util.List;
import java.util.UUID;

public interface VetService {
    List<VetResponse> getAll();

    VetResponse getByUuid(UUID uuid);

    VetResponse getMyProfile();

}
