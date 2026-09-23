package com.github.farzan6118.petclinic.person.mapper;

import com.github.farzan6118.petclinic.person.dto.request.CreateProfileRequestDto;
import com.github.farzan6118.petclinic.person.dto.request.UpdateProfileRequestDto;
import com.github.farzan6118.petclinic.person.dto.response.ProfileResponseDto;
import com.github.farzan6118.petclinic.person.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponseDto toDto(Profile profile) {
        return new ProfileResponseDto(
                profile.getUuid(),
                profile.getEmail(),
                profile.getMobileNumber(),
                profile.getBirthDate(),
                profile.getPhoto()
        );
    }

    public Profile toEntity(CreateProfileRequestDto request) {
        Profile profile = new Profile();
        toEntity(request, profile);
        return profile;
    }

    public void toEntity(CreateProfileRequestDto request, Profile profile) {
        profile.setEmail(request.email());
        profile.setMobileNumber(request.mobileNumber());
        profile.setBirthDate(request.birthDate());
        profile.setPhoto(request.photo());
    }

    public void toEntity(UpdateProfileRequestDto request, Profile profile) {
        profile.setEmail(request.email());
        profile.setMobileNumber(request.mobileNumber());
        profile.setBirthDate(request.birthDate());
        profile.setPhoto(request.photo());
    }
}
