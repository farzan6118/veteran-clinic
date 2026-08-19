package com.github.farzan6118.petclinic.repository.jpa;

import com.github.farzan6118.petclinic.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
