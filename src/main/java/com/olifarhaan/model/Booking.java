package com.olifarhaan.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.olifarhaan.domains.AddOn;
import com.olifarhaan.domains.BookingStatus;
import com.olifarhaan.domains.PaymentStatus;
import com.olifarhaan.request.BookingRequest;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author M. Ali Farhan
 */
@Entity
@Getter
@Setter
@Table(name = "bookings")
@NoArgsConstructor
public class Booking extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "booking_confirmation_code", nullable = false)
    private String bookingConfirmationCode;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "adults_count", nullable = false)
    private int adultsCount;

    @Column(name = "children_count", nullable = false)
    private int childrenCount;

    @ElementCollection
    @CollectionTable(name = "booking_add_on", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "add_on")
    private List<AddOn> addOns = new ArrayList<>();

    @Column(name = "booking_amount", nullable = false)
    private double bookingAmount;

    public Booking(BookingRequest bookingRequest, Room room, User user, double bookingAmount) {
        this.checkInDate = bookingRequest.getCheckInDate();
        this.checkOutDate = bookingRequest.getCheckOutDate();
        this.adultsCount = bookingRequest.getAdultsCount();
        this.childrenCount = bookingRequest.getChildrenCount();
        this.addOns = bookingRequest.getAddOns();
        this.room = room;
        this.user = user;
        this.bookingAmount = bookingAmount;
    }
}
