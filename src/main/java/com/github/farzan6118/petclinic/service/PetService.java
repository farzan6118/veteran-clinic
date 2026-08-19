package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.CreatePetRequest;
import com.github.farzan6118.petclinic.controller.dto.request.UpdatePetRequest;
import com.github.farzan6118.petclinic.controller.dto.response.PetResponse;

import java.util.List;
import java.util.UUID;

public interface PetService {
    PetResponse addPet(CreatePetRequest request);

    List<PetResponse> getMyPets();

    PetResponse getPet(UUID uuid);

    PetResponse updatePet(UUID uuid, UpdatePetRequest request);

    void deletePet(UUID uuid);
}
