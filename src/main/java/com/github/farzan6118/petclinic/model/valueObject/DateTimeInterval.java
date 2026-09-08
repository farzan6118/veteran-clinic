package com.github.farzan6118.petclinic.model.valueObject;

import com.github.farzan6118.petclinic.exception.GenericValidationException;
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
public class DateTimeInterval {

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    private DateTimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = Objects.requireNonNull(start, "Start must not be null");
        this.end = Objects.requireNonNull(end, "End must not be null");

        if (!end.isAfter(start)) {
            throw new GenericValidationException(
                    "End time must be after start time"
            );
        }
    }

    /**
     * Creates an interval from explicit start and end date-times.
     */
    public static DateTimeInterval of(
            LocalDateTime start,
            LocalDateTime end
    ) {
        return new DateTimeInterval(start, end);
    }

    /**
     * Creates an interval from a date, start time and appointment duration.
     */
    public static DateTimeInterval of(
            LocalDate date,
            LocalTime start,
            AppointmentDuration duration
    ) {
        Objects.requireNonNull(date, "Date must not be null");
        Objects.requireNonNull(start, "Start must not be null");
        Objects.requireNonNull(duration, "Duration must not be null");

        LocalDateTime startDateTime = LocalDateTime.of(date, start);
        LocalDateTime endDateTime = startDateTime.plusMinutes(
                duration.getMinutes()
        );

        return new DateTimeInterval(startDateTime, endDateTime);
    }

    /**
     * Creates an interval from a date and explicit start/end times.
     */
    public static DateTimeInterval of(
            LocalDate date,
            LocalTime start,
            LocalTime end
    ) {
        Objects.requireNonNull(date, "Date must not be null");
        Objects.requireNonNull(start, "Start must not be null");
        Objects.requireNonNull(end, "End must not be null");

        return new DateTimeInterval(
                LocalDateTime.of(date, start),
                LocalDateTime.of(date, end)
        );
    }

    public Duration duration() {
        return Duration.between(start, end);
    }

    /**
     * Returns true if this interval overlaps the other interval.
     * <p>
     * Intervals use [start, end) semantics.
     * <p>
     * Example:
     * 09:00 - 09:30
     * 09:30 - 10:00
     * <p>
     * These intervals do not overlap.
     */
    public boolean overlaps(DateTimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return start.isBefore(other.end)
                && other.start.isBefore(end);
    }

    /**
     * Returns true if this interval completely contains
     * the other interval.
     */
    public boolean contains(DateTimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return !other.start.isBefore(start)
                && !other.end.isAfter(end);
    }

    /**
     * Returns true if the given date/time falls inside this interval.
     * <p>
     * Uses [start, end) semantics.
     */
    public boolean contains(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "Date/time must not be null");

        return !dateTime.isBefore(start)
                && dateTime.isBefore(end);
    }

    /**
     * Returns true if this interval is completely before
     * the other interval.
     */
    public boolean isBefore(DateTimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return !end.isAfter(other.start);
    }

    /**
     * Returns true if this interval is completely after
     * the other interval.
     */
    public boolean isAfter(DateTimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return !start.isBefore(other.end);
    }

    /**
     * Returns true if the intervals touch each other
     * without overlapping.
     */
    public boolean isAdjacentTo(DateTimeInterval other) {
        Objects.requireNonNull(other, "Time interval must not be null");

        return end.equals(other.start)
                || start.equals(other.end);
    }

}
