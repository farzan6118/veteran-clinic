package com.github.farzan6118.petclinic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clinic.scheduling")
public record SchedulingProperties(
        int standardDurationMinutes,
        int ownersPlaceDurationMinutes,
        int emergencyDurationMinutes
) {
}