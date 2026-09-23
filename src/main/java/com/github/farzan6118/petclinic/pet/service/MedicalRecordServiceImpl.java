package com.github.farzan6118.petclinic.pet.service;

import com.github.farzan6118.petclinic.appointment.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.model.Visit;
import com.github.farzan6118.petclinic.pet.mapper.MedicalRecordMapper;
import com.github.farzan6118.petclinic.pet.model.MedicalRecord;
import com.github.farzan6118.petclinic.pet.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public void create(MedicalRecord medicalRecord, CompleteVisitRequestDto request, Visit visit) {
        medicalRecordMapper.toEntity(medicalRecord, request, visit);
        repository.save(medicalRecord);
        log.info("Medical Record saved");
    }
}