package com.github.farzan6118.petclinic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmailService {

    void sendCheckupReminder(
            String email,
            String petName,
            LocalDate checkupDate
    );

    void sendVisitScheduledNotification(
            String email,
            String ownerName,
            String petName,
            LocalDateTime visitDate,
            String vetName
    );

    void sendVetAppointmentScheduledNotification(
            String vetEmail,
            String vetName,
            LocalDateTime appointmentDate,
            String petName,
            String petType,
            String ownerName
    );

    void sendVisitRescheduledNotification(
            String email,
            String ownerName,
            String petName,
            String petType,
            String vetName,
            LocalDateTime previousVisitDate,
            LocalDateTime newVisitDate
    );

    void sendVetAppointmentRescheduledNotification(
            String vetEmail,
            String vetName,
            String petName,
            String petType,
            String ownerName,
            LocalDateTime previousAppointmentDate,
            LocalDateTime newAppointmentDate
    );

    void sendVisitCancelledNotification(
            String email,
            String ownerName,
            String petName,
            String petType,
            String vetName,
            LocalDateTime visitDate,
            String cancellationReason
    );

    void sendVetAppointmentCancelledNotification(
            String vetEmail,
            String vetName,
            String petName,
            String ownerName,
            LocalDateTime appointmentDate,
            String cancellationReason
    );
}
