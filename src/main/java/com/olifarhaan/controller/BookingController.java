package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Booking;
import com.olifarhaan.model.User;
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
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date must come before check-out date");
        }
        return ResponseEntity.ok(bookingService.saveBooking(bookingRequest, getLoggedInUser()));
    }

    @GetMapping("/{bookingId}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.user.id == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    public Booking getBookingById(@PathVariable String bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Booking> getAllBookings(@RequestParam(required = false) String userId) {
        if (!getLoggedInUserRole().equals(User.Role.ADMIN)) {
            userId = getLoggedInUserId();
        }
        return bookingService.getAllBookings(userId);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.user.id == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    public Booking getBookingByConfirmationCode(@PathVariable Long confirmationCode) {
        return bookingService.findByBookingConfirmationCode(confirmationCode);
    }

    @PutMapping("/cancel/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.getBookingById(#bookingId).user.id == authentication.name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}
