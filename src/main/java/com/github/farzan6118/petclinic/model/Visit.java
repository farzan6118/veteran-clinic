package com.github.farzan6118.petclinic.model;

import com.github.farzan6118.petclinic.model.constant.VisitStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Audited
public class Visit extends BaseEntity<Long> {

    @ManyToOne(optional = false)
    private Pet pet;

    @ManyToOne(optional = false)
    private Vet vet;

    @Column(nullable = false)
    private LocalDateTime visitDateTime;
    @Column(length = 2048)
    private String notes;
    @Column(length = 2048)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus status;
    private String diagnosis;
}
