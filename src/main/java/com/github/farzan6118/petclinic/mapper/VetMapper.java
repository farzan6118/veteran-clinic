package com.github.farzan6118.petclinic.mapper;

import com.github.farzan6118.petclinic.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.model.Profile;
import com.github.farzan6118.petclinic.model.Vet;
import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
import org.springframework.stereotype.Component;

@Component
public class VetMapper {

    public Vet mapToEntity(CreateVetRequestDto request) {
        if (request == null) {
            return null;
        }
        Vet vet = new Vet();
        vet.setAppointmentDuration(handleAppointmentDuration(request.duration()));
        vet.setFirstname(request.firstName());
        vet.setLastname(request.lastName());
        vet.setNationalId(request.nationalId());
        vet.setMobileNumber(request.mobileNumber());
        vet.setEmail(request.email());
        vet.setAppointmentDuration(request.duration());
        return vet;
    }

    private AppointmentDuration handleAppointmentDuration(AppointmentDuration duration) {
        return duration != null ? duration : AppointmentDuration.FIFTEEN_MINUTES;
    }

    public void mapToEntity(UpdateVetRequestDto request, Vet vet) {
        vet.setAppointmentDuration(handleAppointmentDuration(request.duration()));
        vet.setFirstname(request.firstName());
        vet.setLastname(request.lastName());
        vet.setNationalId(request.nationalId());
        vet.setMobileNumber(request.mobileNumber());
        vet.setEmail(request.email());
        vet.setAppointmentDuration(request.duration());
    }

    public VetResponseDto mapToDto(Vet vet) {
        return new VetResponseDto(
                vet.getUuid(),
                vet.getFullName(),
                vet.getNationalId(),
                vet.getMobileNumber(),
                vet.getEmail(),
                vet.getAppointmentDuration()
        );
    }

    public VetProfileResponseDto mapToVetProfileDto(Vet vet) {
        Profile profile = vet.getProfile() != null ? vet.getProfile() : new Profile();
        return new VetProfileResponseDto(
                vet.getUuid(),
                vet.getFullName(),
                vet.getNationalId(),
                vet.getMobileNumber(),
                vet.getEmail(),
                vet.getAppointmentDuration(),
                profile.getCity(),
                profile.getAddress(),
                profile.getSpecialty(),
                profile.getBirthDate());
    }
}
