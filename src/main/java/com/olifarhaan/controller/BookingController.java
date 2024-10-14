package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/bookings")
public class BookingController extends BaseController {
    private final IBookingService bookingService;

    
    @PostMapping
    public ResponseEntity<Booking> saveBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest, getLoggedInUser()));
    }
    
    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<Booking> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        return ResponseEntity.ok(bookingService.findByBookingConfirmationCode(confirmationCode));
    }
    
    @GetMapping
    public ResponseEntity<List<Booking>> getUserBookings() {
        return ResponseEntity.ok(bookingService.getUserBookings(getLoggedInUserId()));
    }
    
    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @DeleteMapping("/cancel/{bookingId}")
    public void cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}
