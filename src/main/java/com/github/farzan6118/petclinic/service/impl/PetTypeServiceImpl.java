package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.response.PetTypeResponse;
import com.github.farzan6118.petclinic.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {


    @Override
    public List<PetTypeResponse> getAll() {
        return List.of();
    }

    @Override
    public PetTypeResponse getByUuid(UUID uuid) {
        return null;
    }
}

