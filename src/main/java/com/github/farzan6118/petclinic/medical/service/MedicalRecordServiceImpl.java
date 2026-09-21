package com.github.farzan6118.petclinic.medical.service;

import com.github.farzan6118.petclinic.medical.mapper.MedicalRecordMapper;
import com.github.farzan6118.petclinic.medical.model.MedicalRecord;
import com.github.farzan6118.petclinic.medical.repository.MedicalRecordRepository;
import com.github.farzan6118.petclinic.visit.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.visit.model.Visit;
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