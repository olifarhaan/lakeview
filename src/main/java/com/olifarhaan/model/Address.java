package com.olifarhaan.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address extends BaseEntity {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
