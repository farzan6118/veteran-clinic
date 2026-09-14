package com.github.farzan6118.petclinic.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

public record SortRequestDto(

        @Schema(description = "Sort field",
                allowableValues = {"createdDate", "lastModifiedDate"})
        String sortBy,

        @Schema(description = "Sort direction")
        Sort.Direction sortDirection
) {
}