package com.github.farzan6118.petclinic.service;

import com.github.farzan6118.petclinic.controller.dto.request.UpdateOwnerRequest;
import com.github.farzan6118.petclinic.controller.dto.response.OwnerResponse;

public interface OwnerService {

    OwnerResponse getMyProfile();

    OwnerResponse updateMyProfile(
            UpdateOwnerRequest request
    );

}
