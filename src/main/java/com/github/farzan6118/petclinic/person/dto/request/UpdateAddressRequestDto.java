package com.github.farzan6118.petclinic.person.dto.request;

public record UpdateAddressRequestDto(
        String title,
        String countryName,
        String provinceName,
        String cityName,
        String buildingNumber,
        Integer floor,
        String unitNumber,
        String address,
        String postalCode,
        Double latitude,
        Double longitude,
        String description,
        boolean defaultAddress
) {
}
