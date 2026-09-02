package com.github.farzan6118.petclinic.model.constant;

import lombok.Getter;

@Getter
public enum AppointmentDuration {

    FIFTEEN_MINUTES(15),
    THIRTY_MINUTES(30),
    FORTY_FIVE_MINUTES(45),
    ONE_HOUR(60),
    NINETY_MINUTES(90),
    TWO_HOURS(120);

    private final int minutes;

    AppointmentDuration(int minutes) {
        this.minutes = minutes;
    }

}