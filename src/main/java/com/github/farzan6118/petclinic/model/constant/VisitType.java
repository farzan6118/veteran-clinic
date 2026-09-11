package com.github.farzan6118.petclinic.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VisitType {
    ONSITE,
    ONLINE,
    OWNERS_PLACE,
    EMERGENCY;

    @JsonCreator
    public static VisitType fromValue(String value) {
        return switch (value.trim().toLowerCase()) {
            case "onsite", "on-site", "in clinic", "in_clinic" -> ONSITE;
            case "online" -> ONLINE;
            case "owners place", "owner's place", "owners_place", "home visit", "home_visit" -> OWNERS_PLACE;
            case "emergency" -> EMERGENCY;
            default -> throw new IllegalArgumentException("Unsupported visit type: " + value);
        };
    }

    @JsonValue
    public String toValue() {
        return switch (this) {
            case ONSITE -> "onsite";
            case ONLINE -> "online";
            case OWNERS_PLACE -> "owners place";
            case EMERGENCY -> "emergency";
        };
    }
}