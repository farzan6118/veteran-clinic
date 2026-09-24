package com.github.farzan6118.petclinic.vet.repository;

import com.github.farzan6118.petclinic.vet.model.Vet;

import java.time.LocalDateTime;
import java.util.List;

public interface VetQueryRepository {

    List<Vet> findAvailableVets(LocalDateTime start, LocalDateTime end);

}
