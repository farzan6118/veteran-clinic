package com.github.farzan6118.petclinic.impl;

import com.github.farzan6118.petclinic.common.enums.VisitStatus;
import com.github.farzan6118.petclinic.common.enums.VisitType;
import com.github.farzan6118.petclinic.common.exception.GenericValidationException;
import com.github.farzan6118.petclinic.common.exception.ResourceNotFoundException;
import com.github.farzan6118.petclinic.infrastructure.email.VisitNotificationService;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.service.PetService;
import com.github.farzan6118.petclinic.room.model.Room;
import com.github.farzan6118.petclinic.room.repository.RoomRepository;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.vet.repository.VetRepository;
import com.github.farzan6118.petclinic.visit.dto.request.RescheduleVisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.VisitRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.VisitResponseDto;
import com.github.farzan6118.petclinic.visit.mapper.VisitMapper;
import com.github.farzan6118.petclinic.visit.model.Visit;
import com.github.farzan6118.petclinic.visit.repository.VisitRepository;
import com.github.farzan6118.petclinic.visit.service.VisitServiceImpl;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceImplTest {

    @Mock
    private VisitNotificationService visitNotificationService;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PetService petService;

    @Mock
    private VisitMapper visitMapper;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private VetAvailabilityRepository availabilityRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private VisitServiceImpl service;

    private UUID vetUuid;
    private UUID petUuid;
    private UUID visitUuid;

    private Vet vet;
    private Pet pet;
    private Room room;

    @BeforeEach
    void setUp() {
        vetUuid = UUID.randomUUID();
        petUuid = UUID.randomUUID();
        visitUuid = UUID.randomUUID();

        vet = new Vet();
        vet.setUuid(vetUuid);

        pet = new Pet();
        pet.setUuid(petUuid);

        room = new Room();
        room.setUuid(UUID.randomUUID());

        Visit savedVisit = new Visit();
        savedVisit.setUuid(visitUuid);
    }

    // -------------------------------------------------------------------------
    // bookVisit
    // -------------------------------------------------------------------------

    @Test
    void shouldBookOnsiteVisitSuccessfully() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid, vetUuid, date, time, VisitType.ONSITE, "General examination");

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));
        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                eq(LocalDateTime.of(date, time)),
                eq(LocalDateTime.of(date, time.plusMinutes(10)))
        )).thenReturn(true);

        when(roomRepository.findActiveRoomsByTypeNames(List.of("examination", "individual")))
                .thenReturn(List.of(room));

        when(visitRepository.existsRoomReservation(
                eq(room.getUuid()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                isNull()
        )).thenReturn(false);

        when(visitRepository.save(any(Visit.class)))
                .thenAnswer(invocation -> {
                    Visit saved = invocation.getArgument(0);
                    saved.setUuid(visitUuid);
                    return saved;
                });

        service.bookVisit(request);

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(captor.capture());

        Visit savedVisit = captor.getValue();

        assertEquals(vet, savedVisit.getVet());
        assertEquals(pet, savedVisit.getPet());
        assertEquals(room, savedVisit.getRoom());
        assertEquals(VisitType.ONSITE, savedVisit.getVisitType());
        assertEquals(VisitStatus.SCHEDULED, savedVisit.getStatus());
        assertEquals(LocalDateTime.of(date, time), savedVisit.getStartTime());
        assertEquals(
                LocalDateTime.of(date, time.plusMinutes(10)),
                savedVisit.getEndTime()
        );

        verify(visitNotificationService)
                .notifyBookVisitParticipants(savedVisit, pet, vet);
    }

    @Test
    void shouldBookOnlineVisitWithoutRoom() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                time,
                VisitType.ONLINE,
                "Online consultation"
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));
        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        when(visitRepository.save(any(Visit.class)))
                .thenAnswer(invocation -> {
                    Visit saved = invocation.getArgument(0);
                    saved.setUuid(visitUuid);
                    return saved;
                });

        service.bookVisit(request);

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(captor.capture());

        Visit savedVisit = captor.getValue();

        assertNull(savedVisit.getRoom());
        assertEquals(VisitType.ONLINE, savedVisit.getVisitType());

        verify(roomRepository, never())
                .findActiveRoomsByTypeNames(anyList());

        verify(visitNotificationService)
                .notifyBookVisitParticipants(savedVisit, pet, vet);
    }

    @Test
    void shouldBookOwnersPlaceVisitWithoutRoom() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                time,
                VisitType.OFFSITE,
                "Home visit"
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));
        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        when(visitRepository.save(any(Visit.class)))
                .thenAnswer(invocation -> {
                    Visit saved = invocation.getArgument(0);
                    saved.setUuid(visitUuid);
                    return saved;
                });

        service.bookVisit(request);

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(captor.capture());

        assertNull(captor.getValue().getRoom());
        assertEquals(VisitType.OFFSITE, captor.getValue().getVisitType());

        verify(roomRepository, never())
                .findActiveRoomsByTypeNames(anyList());
    }

    @Test
    void shouldBookEmergencyVisitUsingEmergencyRoom() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                time,
                VisitType.ONSITE,
                "Emergency"
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));

        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        when(roomRepository.findActiveRoomsByTypeNames(
                List.of("surgery", "emergency", "isolation")
        )).thenReturn(List.of(room));

        when(visitRepository.existsRoomReservation(
                eq(room.getUuid()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                isNull()
        )).thenReturn(false);

        when(visitRepository.save(any(Visit.class)))
                .thenAnswer(invocation -> {
                    Visit saved = invocation.getArgument(0);
                    saved.setUuid(visitUuid);
                    return saved;
                });

        service.bookVisit(request);

        verify(roomRepository)
                .findActiveRoomsByTypeNames(
                        List.of("surgery", "emergency", "isolation")
                );

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(captor.capture());

        assertEquals(room, captor.getValue().getRoom());
        assertEquals(VisitType.ONSITE, captor.getValue().getVisitType());
    }

    @Test
    void shouldRejectBookingWhenPetDoesNotExist() {
        LocalDate date = LocalDate.now().plusDays(1);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                LocalTime.of(10, 0),
                VisitType.ONLINE,
                null
        );

        when(petService.getEntityByUuid(petUuid))
                .thenThrow(new ResourceNotFoundException("pet.not.found"));

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.bookVisit(request)
        );

        verify(vetRepository, never()).findByUuidWithLock(any());
        verify(visitRepository, never()).save(any());
    }

    @Test
    void shouldRejectBookingWhenVetDoesNotExist() {
        LocalDate date = LocalDate.now().plusDays(1);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                LocalTime.of(10, 0),
                VisitType.ONLINE,
                null
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.bookVisit(request)
        );

        verify(availabilityRepository, never())
                .existsCoveringTime(any(), any(), any());

        verify(visitRepository, never()).save(any());
    }

    @Test
    void shouldRejectBookingWhenVetIsNotAvailable() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                time,
                VisitType.ONLINE,
                null
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));

        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.bookVisit(request)
        );

        verify(visitRepository, never()).save(any());
        verify(visitNotificationService, never())
                .notifyBookVisitParticipants(any(), any(), any());
    }

    @Test
    void shouldRejectBookingWhenRoomIsNotAvailable() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        VisitRequestDto request = new VisitRequestDto(
                petUuid,
                vetUuid,
                date,
                time,
                VisitType.ONSITE,
                null
        );

        when(petService.getEntityByUuid(petUuid)).thenReturn(pet);
        when(vetRepository.findByUuidWithLock(vetUuid)).thenReturn(Optional.of(vet));

        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        when(roomRepository.findActiveRoomsByTypeNames(
                List.of("examination", "individual")
        )).thenReturn(List.of(room));

        when(visitRepository.existsRoomReservation(
                eq(room.getUuid()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                isNull()
        )).thenReturn(true);

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.bookVisit(request)
        );

        verify(visitRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // cancelVisit
    // -------------------------------------------------------------------------

    @Test
    void shouldCancelScheduledVisitAndNotifyParticipants() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                "test"
        );
        visit.setUuid(visitUuid);

        when(visitRepository.findByUuid(visitUuid))
                .thenReturn(Optional.of(visit));

        service.cancelVisit(visitUuid, "Customer request");

        assertEquals(VisitStatus.CANCELLED, visit.getStatus());

        verify(visitNotificationService)
                .notifyCancelVisitParticipants(
                        visit,
                        pet,
                        vet,
                        "Customer request"
                );
    }

    @Test
    void shouldDoNothingWhenVisitIsAlreadyCancelled() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.CANCELLED);

        when(visitRepository.findByUuid(visitUuid))
                .thenReturn(Optional.of(visit));

        service.cancelVisit(visitUuid, "Already cancelled");

        verify(visitNotificationService, never())
                .notifyCancelVisitParticipants(any(), any(), any(), anyString());
    }

    @Test
    void shouldRejectCancellingCompletedVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findByUuid(visitUuid))
                .thenReturn(Optional.of(visit));

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.cancelVisit(visitUuid, "Too late")
        );

        verify(visitNotificationService, never())
                .notifyCancelVisitParticipants(any(), any(), any(), anyString());
    }

    @Test
    void shouldRejectCancellingUnknownVisit() {
        when(visitRepository.findByUuid(visitUuid))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.cancelVisit(visitUuid, "reason")
        );
    }

    // -------------------------------------------------------------------------
    // completeVisit
    // -------------------------------------------------------------------------

    @Test
    void shouldCompleteStartedVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                VisitType.ONLINE,
                null
        );

        visit.setUuid(visitUuid);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        VisitResponseDto response = mock(VisitResponseDto.class);

        when(visitMapper.toResponse(visit)).thenReturn(response);

        service.completeVisit(visitUuid, null);

        assertEquals(VisitStatus.COMPLETED, visit.getStatus());

        verify(visitRepository).findByUuidForUpdate(visitUuid);
        verify(visitMapper).toResponse(visit);
    }

    @Test
    void shouldRejectCompletingCancelledVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.CANCELLED);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        assertThrows(
                GenericValidationException.class,
                () -> service.completeVisit(visitUuid, null)
        );
    }

    @Test
    void shouldRejectCompletingAlreadyCompletedVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        assertThrows(
                GenericValidationException.class,
                () -> service.completeVisit(visitUuid, null)
        );
    }

    @Test
    void shouldRejectCompletingVisitThatHasNotStarted() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(20),
                VisitType.ONLINE,
                null
        );

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        assertThrows(
                GenericValidationException.class,
                () -> service.completeVisit(visitUuid, null)
        );
    }

    @Test
    void shouldRejectCompletingVisitThatAlreadyFinished() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().minusMinutes(20),
                LocalDateTime.now().minusMinutes(10),
                VisitType.ONLINE,
                null
        );

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        assertThrows(
                GenericValidationException.class,
                () -> service.completeVisit(visitUuid, null)
        );
    }

    // -------------------------------------------------------------------------
    // rescheduleVisit
    // -------------------------------------------------------------------------

    @Test
    void shouldRescheduleVisitSuccessfully() {
        LocalDateTime oldStart = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime oldEnd = oldStart.plusMinutes(10);

        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                oldStart,
                oldEnd,
                VisitType.ONLINE,
                "old description"
        );

        visit.setUuid(visitUuid);

        LocalDate newDate = LocalDate.now().plusDays(2);
        LocalTime newTime = LocalTime.of(14, 0);

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        newDate,
                        newTime,
                        "new description",
                        "customer request"
                );

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        when(vetRepository.findByUuidWithLock(vetUuid))
                .thenReturn(Optional.of(vet));

        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        when(visitMapper.toResponse(visit))
                .thenReturn(mock(VisitResponseDto.class));

        service.rescheduleVisit(visitUuid, request);

        assertEquals(
                LocalDateTime.of(newDate, newTime),
                visit.getStartTime()
        );

        assertEquals(
                "new description",
                visit.getDescription()
        );

        verify(visitRepository).findByUuidForUpdate(visitUuid);

        verify(vetRepository).findByUuidWithLock(vetUuid);

        verify(visitNotificationService)
                .notifyRescheduleVisitParticipants(
                        eq(visit),
                        eq(pet),
                        eq(vet),
                        eq(oldStart)
                );
    }

    @Test
    void shouldRejectReschedulingUnknownVisit() {
        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.empty());

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0),
                        "new",
                        "reason"
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.rescheduleVisit(visitUuid, request)
        );
    }

    @Test
    void shouldRejectReschedulingCancelledVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.CANCELLED);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0),
                        null,
                        null
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.rescheduleVisit(visitUuid, request)
        );

        verify(vetRepository, never()).findByUuidWithLock(any());
    }

    @Test
    void shouldRejectReschedulingCompletedVisit() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0),
                        null,
                        null
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.rescheduleVisit(visitUuid, request)
        );
    }

    @Test
    void shouldRejectReschedulingToPast() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setUuid(visitUuid);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        when(vetRepository.findByUuidWithLock(vetUuid))
                .thenReturn(Optional.of(vet));

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        LocalDate.now().minusDays(1),
                        LocalTime.of(10, 0),
                        null,
                        null
                );

        assertThrows(
                GenericValidationException.class,
                () -> service.rescheduleVisit(visitUuid, request)
        );

        verify(availabilityRepository, never())
                .existsCoveringTime(any(), any(), any());
    }

    @Test
    void shouldRejectReschedulingWhenVetIsUnavailable() {
        Visit visit = new Visit().schedule(
                vet,
                pet,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusMinutes(10),
                VisitType.ONLINE,
                null
        );

        visit.setUuid(visitUuid);

        when(visitRepository.findByUuidForUpdate(visitUuid))
                .thenReturn(Optional.of(visit));

        when(vetRepository.findByUuidWithLock(vetUuid))
                .thenReturn(Optional.of(vet));

        when(availabilityRepository.existsCoveringTime(
                eq(vetUuid),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(false);

        RescheduleVisitRequestDto request =
                new RescheduleVisitRequestDto(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0),
                        null,
                        null
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.rescheduleVisit(visitUuid, request)
        );
    }

    // -------------------------------------------------------------------------
    // simple query methods
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnVisitByUuid() {
        Visit visit = mock(Visit.class);
        VisitResponseDto response = mock(VisitResponseDto.class);

        when(visitRepository.findByUuid(visitUuid))
                .thenReturn(Optional.of(visit));

        when(visitMapper.toResponse(visit))
                .thenReturn(response);

        assertSame(response, service.getByUuid(visitUuid));
    }

    @Test
    void shouldReturnAllVisits() {
        Visit first = mock(Visit.class);
        Visit second = mock(Visit.class);

        VisitResponseDto firstResponse = mock(VisitResponseDto.class);
        VisitResponseDto secondResponse = mock(VisitResponseDto.class);

        when(visitRepository.findAll())
                .thenReturn(List.of(first, second));

        when(visitMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(visitMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<VisitResponseDto> result = service.getAllVisits();

        assertEquals(
                List.of(firstResponse, secondResponse),
                result
        );
    }
}