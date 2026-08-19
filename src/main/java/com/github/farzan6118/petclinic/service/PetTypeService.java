package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.response.PetTypeResponse;

import java.util.List;
import java.util.UUID;

public interface PetTypeService {

    List<PetTypeResponse> getAll();

    PetTypeResponse getByUuid(UUID uuid);

//    PetTypeResponse create(CreatePetTypeRequest request);
//
//    PetTypeResponse update(
//            UUID uuid,
//            UpdatePetTypeRequest request
//    );
//
//    void delete(UUID uuid);
}
