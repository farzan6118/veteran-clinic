package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.controller.dto.request.CompleteVisitRequest;
import com.github.farzan6118.petclinic.controller.dto.request.CreateVisitRequest;
import com.github.farzan6118.petclinic.controller.dto.response.VisitResponse;
import com.github.farzan6118.petclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    @Override
    public VisitResponse bookVisit(CreateVisitRequest request) {
        return null;
    }

    @Override
    public List<VisitResponse> getMyVisits() {
        return List.of();
    }

    @Override
    public VisitResponse getByUuid(UUID uuid) {
        return null;
    }

    @Override
    public void cancelVisit(UUID uuid) {

    }

    @Override
    public List<VisitResponse> getVetVisits() {
        return List.of();
    }

    @Override
    public VisitResponse completeVisit(UUID uuid, CompleteVisitRequest request) {
        return null;
    }
}

