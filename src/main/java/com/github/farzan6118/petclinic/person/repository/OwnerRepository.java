package com.github.farzan6118.petclinic.person.repository;

import com.github.farzan6118.petclinic.owner.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByUuid(UUID uuid);

    boolean existsByPerson_Profile_Email(String personProfileEmail);

    boolean existsByPerson_Profile_MobileNumber(String mobile);

    boolean existsByPerson_Profile_EmailAndUuidNot(String email, UUID uuid);

    boolean existsByPerson_Profile_MobileNumberAndUuidNot(String telephone, UUID uuid);
}
