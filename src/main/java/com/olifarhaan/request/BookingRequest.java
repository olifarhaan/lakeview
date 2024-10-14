package com.olifarhaan.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.olifarhaan.domains.AddOn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class BookingRequest {
    @NotBlank
    private String roomClassId;
    @NotBlank
    private LocalDate checkInDate;
    @NotBlank
    private LocalDate checkOutDate;
    @PositiveOrZero
    private int adultsCount;
    @PositiveOrZero
    private int childrenCount;
    private List<AddOn> addOns = new ArrayList<>();
}
