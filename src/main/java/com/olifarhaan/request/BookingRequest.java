package com.olifarhaan.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.olifarhaan.domains.AddOn;
import com.olifarhaan.domains.BookingStatus;
import com.olifarhaan.domains.PaymentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class BookingRequest {
    @NotBlank
    private String roomClassId;
    @NotNull
    private LocalDate checkInDate;
    @NotNull
    private LocalDate checkOutDate;
    @PositiveOrZero
    private int guestCount;
    private List<AddOn> addOns = new ArrayList<>();
    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;
    private PaymentStatus paymentStatus = PaymentStatus.PAID;
}
