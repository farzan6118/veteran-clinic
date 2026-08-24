package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetTypeRepository extends JpaRepository<PetType, Integer> {
    List<PetType> findByName(String name);
}
