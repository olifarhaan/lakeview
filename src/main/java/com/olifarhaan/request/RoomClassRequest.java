package com.olifarhaan.request;

import java.util.ArrayList;
import java.util.List;

import com.olifarhaan.domains.BedType;
import com.olifarhaan.domains.Feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class RoomClassRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Positive
    private double basePrice;
    @Positive
    private int maxGuestCount;
    private List<String> images = new ArrayList<>();
    @NotEmpty
    private List<Feature> features;
    @NotEmpty
    private List<BedType> bedTypes;
}
