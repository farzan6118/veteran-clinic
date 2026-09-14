package com.github.farzan6118.petclinic.vet.repository;

import com.github.farzan6118.petclinic.vet.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
