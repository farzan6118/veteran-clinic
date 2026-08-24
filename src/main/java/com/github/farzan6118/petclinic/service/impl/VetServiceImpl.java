package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.response.VetResponse;
import com.github.farzan6118.petclinic.service.VetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    @Override
    public List<VetResponse> getAll() {
        return List.of();
    }

    @Override
    public VetResponse getByUuid(UUID uuid) {
        return null;
    }

    @Override
    public VetResponse getMyProfile() {
        return null;
    }
}

