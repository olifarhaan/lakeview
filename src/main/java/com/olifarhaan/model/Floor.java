package com.olifarhaan.model;

import com.olifarhaan.request.FloorRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "floors")
public class Floor extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    public Floor(FloorRequest floorRequest) {
        this.name = floorRequest.getName();
    }
}
