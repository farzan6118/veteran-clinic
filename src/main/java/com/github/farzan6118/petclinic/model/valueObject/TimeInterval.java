//package com.github.farzan6118.petclinic.model.valueObject;
//
//import com.github.farzan6118.petclinic.model.constant.AppointmentDuration;
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.Duration;
//import java.time.LocalTime;
//import java.util.Objects;
//
//@Embeddable
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class TimeInterval {
//
//    @Column(name = "start_time", nullable = false)
//    private LocalTime start;
//
//    @Column(name = "end_time", nullable = false)
//    private LocalTime end;
//
//    public TimeInterval(LocalTime start, LocalTime end) {
//        this.start = Objects.requireNonNull(start, "Start time must not be null");
//        this.end = Objects.requireNonNull(end, "End time must not be null");
//
//        if (!start.isBefore(end)) {
//            throw new IllegalArgumentException("Start time must be before end time");
//        }
//    }
//
//    public TimeInterval(LocalTime start) {
//        this(start, AppointmentDuration.FIFTEEN_MINUTES);
//    }
//
//    public TimeInterval(LocalTime start, AppointmentDuration duration) {
//        Objects.requireNonNull(duration, "Appointment duration must not be null");
//
//        this.start = Objects.requireNonNull(start, "Start time must not be null");
//
//        this.end = start.plusMinutes(duration.getMinutes());
//    }
//
//    public long durationMinutes() {
//        return Duration.between(start, end).toMinutes();
//    }
//
//    public boolean overlaps(TimeInterval other) {
//        Objects.requireNonNull(other, "Time interval must not be null");
//
//        return start.isBefore(other.end) && other.start.isBefore(end);
//    }
//
//    public boolean contains(TimeInterval other) {
//        Objects.requireNonNull(other, "Time interval must not be null");
//
//        return !other.start.isBefore(start) && !other.end.isAfter(end);
//    }
//
//    public boolean contains(LocalTime time) {
//        Objects.requireNonNull(time, "Time must not be null");
//
//        return !time.isBefore(start) && time.isBefore(end);
//    }
//}