package com.github.farzan6118.petclinic.impl;

import com.github.farzan6118.petclinic.appointment.dto.request.CompleteVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.CreateVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.appointment.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.appointment.model.Visit;
import com.github.farzan6118.petclinic.appointment.repository.VisitRepository;
import com.github.farzan6118.petclinic.appointment.service.DurationTemplateService;
import com.github.farzan6118.petclinic.appointment.service.VisitServiceCommandImpl;
import com.github.farzan6118.petclinic.clinic.model.Room;
import com.github.farzan6118.petclinic.clinic.service.RoomService;
import com.github.farzan6118.petclinic.common.enums.VisitCategory;
import com.github.farzan6118.petclinic.common.enums.VisitStatus;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.config.ClinicProperties;
import com.github.farzan6118.petclinic.infrastructure.email.VisitNotificationService;
import com.github.farzan6118.petclinic.pet.model.MedicalRecord;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.service.MedicalRecordService;
import com.github.farzan6118.petclinic.pet.service.PetService;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.service.VetAvailabilityService;
import com.github.farzan6118.petclinic.vet.service.VetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceImplTest {

    @Mock private VisitRepository visitRepository;
    @Mock private ClinicProperties clinicProperties;
    @Mock private RoomService roomService;
    @Mock private PetService petService;
    @Mock private VetService vetService;
    @Mock private VetAvailabilityService vetAvailabilityService;
    @Mock private DurationTemplateService durationTemplateService;
    @Mock private VisitNotificationService visitNotificationService;
    @Mock private MedicalRecordService medicalRecordService;

    @InjectMocks
    private VisitServiceCommandImpl service;

    private final UUID petUuid = UUID.randomUUID();
    private final UUID vetUuid = UUID.randomUUID();
    private final UUID visitUuid = UUID.randomUUID();

    private Pet pet;
    private Vet vet;
    private Room room;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        pet.setUuid(petUuid);

        vet = new Vet();
        vet.setUuid(vetUuid);

        room = new Room();
        room.setUuid(UUID.randomUUID());
        room.setName("Examination room");

        lenient().when(durationTemplateService.findByName("STANDARD"))
                .thenReturn(new DurationTemplateResponseDto(
                        UUID.randomUUID(), "STANDARD", 10, "Standard visit"));
        lenient().when(vetAvailabilityService.findAvailableByUuidAndTimeRange(any(), any(), any()))
                .thenReturn(Optional.of(vet));
        lenient().when(clinicProperties.closeDays()).thenReturn(java.util.Set.of());
        lenient().when(clinicProperties.workingHours())
                .thenReturn(new ClinicProperties.WorkingHours(LocalTime.of(8, 0), LocalTime.of(17, 0)));
    }

    /**
     * Verifies that booking an onsite visit resolves the pet, veterinarian, and room,
     * creates a scheduled visit with the configured duration, and notifies participants.
     */
    @Test
    void bookVisit_shouldSaveVisitAndNotifyParticipants() {
        LocalDate visitDate = LocalDate.now().plusDays(1);
        LocalTime visitTime = LocalTime.of(10, 0);
        CreateVisitRequestDto request = new CreateVisitRequestDto(
                petUuid, vetUuid, visitDate, visitTime, VisitType.ONSITE, "General examination");

        when(vetService.getVetWithUuidLock(vetUuid)).thenReturn(vet);
        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(roomService.getAvailableRoomByVisitTypeAndVisitCategory(
                VisitType.ONSITE, VisitCategory.ROUTINE)).thenReturn(room);
        when(visitRepository.save(any(Visit.class))).thenAnswer(invocation -> {
            Visit visit = invocation.getArgument(0);
            visit.setUuid(visitUuid);
            return visit;
        });

        service.bookVisit(request);

        ArgumentCaptor<Visit> visitCaptor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(visitCaptor.capture());

        Visit savedVisit = visitCaptor.getValue();
        assertEquals(pet, savedVisit.getPet());
        assertEquals(vet, savedVisit.getVet());
        assertEquals(room, savedVisit.getRoom());
        assertEquals(VisitStatus.SCHEDULED, savedVisit.getStatus());
        assertEquals(LocalDateTime.of(visitDate, visitTime), savedVisit.getStartTime());
        assertEquals(LocalDateTime.of(visitDate, visitTime.plusMinutes(10)), savedVisit.getEndTime());
        verify(visitNotificationService).notifyBookVisitParticipants(savedVisit, pet, vet);
    }

    /**
     * Verifies that rescheduling changes the visit time and description, reuses the
     * availability and conflict checks, saves the updated visit, and sends a notification.
     */
    @Test
    void rescheduleVisit_shouldUpdateSlotAndNotifyParticipants() {
        LocalDateTime oldStart = LocalDateTime.now().minusHours(1);
        Visit visit = new Visit().schedule(
                vet, pet, room, oldStart, oldStart.plusMinutes(10), VisitType.ONSITE, "Original visit");
        visit.setUuid(visitUuid);

        LocalDate newDate = LocalDate.now().plusDays(1);
        LocalTime newTime = LocalTime.of(11, 0);
        RescheduleVisitRequestDto request = new RescheduleVisitRequestDto(
                newDate, newTime, VisitType.ONSITE, "Updated visit", "Owner requested another time");

        when(visitRepository.findByUuidForUpdate(visitUuid)).thenReturn(Optional.of(visit));
        when(vetService.getVetWithUuidLock(vetUuid)).thenReturn(vet);
        when(roomService.getAvailableRoomByVisitTypeAndVisitCategory(
                VisitType.ONSITE, VisitCategory.ROUTINE)).thenReturn(room);

        service.rescheduleVisit(visitUuid, request);

        assertEquals(LocalDateTime.of(newDate, newTime), visit.getStartTime());
        assertEquals(LocalDateTime.of(newDate, newTime.plusMinutes(10)), visit.getEndTime());
        assertEquals("Updated visit", visit.getDescription());
        verify(visitRepository).save(visit);
        verify(visitNotificationService).notifyRescheduleVisitParticipants(
                oldStart, visit, pet, vet, LocalDateTime.of(newDate, newTime));
    }

    /**
     * Verifies that cancelling an active visit changes its status to CANCELLED and
     * notifies the pet owner and veterinarian with the cancellation reason.
     */
    @Test
    void cancelVisit_shouldCancelVisitAndNotifyParticipants() {
        Visit visit = new Visit().schedule(
                vet, pet, room,
                LocalDateTime.now().minusMinutes(20),
                LocalDateTime.now().plusMinutes(20),
                VisitType.ONSITE,
                "Visit");
        visit.setUuid(visitUuid);

        when(visitRepository.findByUuid(visitUuid)).thenReturn(Optional.of(visit));

        service.cancelVisit(visitUuid, "Owner cancelled");

        assertEquals(VisitStatus.CANCELLED, visit.getStatus());
        verify(visitNotificationService).notifyCancelVisitParticipants(
                visit, pet, vet, "Owner cancelled");
    }

    /**
     * Verifies that completing an in-progress visit changes its status to COMPLETED
     * and delegates creation of the associated medical record to the medical service.
     */
    @Test
    void completeVisit_shouldCompleteVisitAndCreateMedicalRecord() {
        Visit visit = new Visit().schedule(
                vet, pet, room,
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().plusMinutes(10),
                VisitType.ONSITE,
                "Visit");
        visit.setUuid(visitUuid);

        CompleteVisitRequestDto request = new CompleteVisitRequestDto(
                "Ear infection", "Inflammation observed", null,
                "Medication for seven days", "Keep the ear clean", true,
                LocalDate.now().plusDays(14), null, null);

        when(visitRepository.findByUuidForUpdate(visitUuid)).thenReturn(Optional.of(visit));

        service.completeVisit(visitUuid, request);

        assertEquals(VisitStatus.COMPLETED, visit.getStatus());
        ArgumentCaptor<MedicalRecord> recordCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordService).create(recordCaptor.capture(), eq(request), eq(visit));
        assertNotNull(recordCaptor.getValue());
    }
}
