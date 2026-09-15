package com.github.farzan6118.petclinic.visit.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleVisitRequestDto(
        @NotNull(message = "visit.date.is.required")
        @FutureOrPresent(message = "visit.date.must.be.in.present.or.future")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate visitDate,
        @NotNull(message = "visit.time.is.required")
        @JsonFormat(pattern = "HH:mm")
        @Schema(example = "09:30")
        LocalTime visitTime,
        @Size(max = 2048, message = "description.too.long")
        String description,
        @Size(max = 255, message = "reason.too.long")
        String reason

) {
}
