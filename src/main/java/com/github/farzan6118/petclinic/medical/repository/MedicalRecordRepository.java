package com.github.farzan6118.petclinic.medical.repository;

import com.github.farzan6118.petclinic.medical.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
