package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Booking;
import com.olifarhaan.request.BookingRequest;
import com.olifarhaan.service.interfaces.IBookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController extends BaseController {
    private final IBookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> saveBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest, getLoggedInUser()));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getUserBookings() {
        return ResponseEntity.ok(bookingService.getUserBookings(getLoggedInUserId()));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<Booking> getBookingByConfirmationCode(@PathVariable Long confirmationCode) {
        return ResponseEntity.ok(bookingService.findByBookingConfirmationCode(confirmationCode));
    }

    @DeleteMapping("/cancel/{bookingId}")
    public void cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}
