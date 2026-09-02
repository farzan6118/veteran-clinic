package com.github.farzan6118.petclinic.service;

import java.time.LocalDate;
import java.util.UUID;

public interface AppointmentSlotService {

    void generateUpcomingSlots(UUID vetUuid, LocalDate from, LocalDate to);

    void generateSlotsForDate(UUID vetUuid, LocalDate date);

}
