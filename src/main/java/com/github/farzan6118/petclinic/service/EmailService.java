package com.github.farzan6118.petclinic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmailService {

    void sendCheckupReminder(String email, String petName, LocalDate checkupDate);

    void sendVisitReminder(String email, String petName, LocalDateTime visitDate, String vetName);

    void sendVetAppointmentReminder(String email, String vetName, LocalDateTime appointmentDate);
}
