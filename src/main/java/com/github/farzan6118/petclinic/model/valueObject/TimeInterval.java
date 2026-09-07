package com.github.farzan6118.petclinic.model.valueObject;

import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeInterval {

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    public TimeInterval(
            LocalTime start,
            LocalDate date,
            AppointmentDuration duration
    ) {
        Objects.requireNonNull(start, "Start must not be null");
        Objects.requireNonNull(date, "Date must not be null");
        Objects.requireNonNull(duration, "Duration must not be null");

        this.start = LocalDateTime.of(date, start);
        this.end = this.start.plusMinutes(duration.getMinutes());
    }

    public long durationMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public boolean overlaps(TimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return start.isBefore(other.end)
                && other.start.isBefore(end);
    }

    public boolean contains(TimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return !other.start.isBefore(start)
                && !other.end.isAfter(end);
    }
}