package com.github.farzan6118.petclinic.clinic.model;

import com.github.farzan6118.petclinic.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoomType extends BaseEntity<Integer> {
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
}