package com.github.farzan6118.petclinic.vet.mapper;

import com.github.farzan6118.petclinic.common.enums.AppointmentDuration;
import com.github.farzan6118.petclinic.vet.dto.request.CreateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.request.UpdateVetRequestDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetProfileResponseDto;
import com.github.farzan6118.petclinic.vet.dto.response.VetResponseDto;
import com.github.farzan6118.petclinic.vet.model.Profile;
import com.github.farzan6118.petclinic.vet.model.Vet;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class VetMapper {

    public Vet mapToEntity(CreateVetRequestDto request) {
        if (request == null) {
            return null;
        }
        Vet vet = new Vet();
        vet.setFirstName(normalizeName(request.firstName()));
        vet.setLastName(normalizeName(request.lastName()));
        vet.setNationalId(request.nationalId());
        vet.setMobileNumber(request.mobileNumber());
        vet.setEmail(request.email());
        return vet;
    }

    private AppointmentDuration handleAppointmentDuration(AppointmentDuration duration) {
        return duration != null ? duration : AppointmentDuration.FIFTEEN_MINUTES;
    }

    public void mapToEntity(UpdateVetRequestDto request, Vet vet) {
        vet.setFirstName(normalizeName(request.firstName()));
        vet.setLastName(normalizeName(request.lastName()));
        vet.setNationalId(request.nationalId());
        vet.setMobileNumber(request.mobileNumber());
        vet.setEmail(request.email());
    }

    public VetResponseDto mapToDto(Vet vet) {
        return new VetResponseDto(
                vet.getUuid(),
                vet.getFullName(),
                vet.getNationalId(),
                vet.getMobileNumber(),
                vet.getEmail()
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
                profile.getCity(),
                profile.getAddress(),
                profile.getSpecialty(),
                profile.getBirthDate());
    }

    private String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
}
