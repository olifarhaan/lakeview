package com.olifarhaan.service.interfaces;

import java.util.List;

import com.olifarhaan.model.Booking;
import com.olifarhaan.model.User;
import com.olifarhaan.request.BookingRequest;

/**
 * @author M. Ali Farhan
 */

public interface IBookingService {
    void cancelBooking(String bookingId);

    Booking getBookingById(String bookingId);

    List<Booking> getAllBookingsByRoomId(String roomId);

    Booking saveBooking(BookingRequest bookingRequest, User loggedInUser);

    Booking findByBookingConfirmationCode(String confirmationCode);

    List<Booking> getAllBookings();

    List<Booking> getUserBookings(String userId);
}
