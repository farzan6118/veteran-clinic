package com.github.farzan6118.petclinic.pet.repository;

import com.github.farzan6118.petclinic.pet.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
