package com.github.farzan6118.petclinic.visit.mapper;

import com.github.farzan6118.petclinic.visit.dto.request.CreateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.request.UpdateDurationTemplateRequestDto;
import com.github.farzan6118.petclinic.visit.dto.response.DurationTemplateResponseDto;
import com.github.farzan6118.petclinic.visit.model.DurationTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DurationTemplateMapper {

    public void mapToEntity(CreateDurationTemplateRequestDto request, DurationTemplate durationTemplate) {
        durationTemplate.setName(request.name().toUpperCase(Locale.ROOT));
        durationTemplate.setDurationMinutes(request.durationMinutes());
        durationTemplate.setDescription(request.description());
    }

    public void mapToEntity(UpdateDurationTemplateRequestDto request, DurationTemplate durationTemplate) {
        durationTemplate.setName(request.name().toUpperCase(Locale.ROOT));
        durationTemplate.setDurationMinutes(request.durationMinutes());
        durationTemplate.setDescription(request.description());
    }

    public DurationTemplateResponseDto mapToDto(DurationTemplate template) {
        return new DurationTemplateResponseDto(
                template.getUuid(),
                template.getName(),
                template.getDurationMinutes(),
                template.getDescription()
        );
    }
}
