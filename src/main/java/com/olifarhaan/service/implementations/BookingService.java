package com.olifarhaan.service.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.domains.AddOn;
import com.olifarhaan.domains.BookingStatus;
import com.olifarhaan.model.Booking;
import com.olifarhaan.model.Room;
import com.olifarhaan.model.RoomClass;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.BookingRepository;
import com.olifarhaan.request.BookingRequest;
import com.olifarhaan.service.interfaces.IBookingService;
import com.olifarhaan.service.interfaces.IRoomService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No booking found with booking id :" + bookingId));
    }

    @Override
    public void cancelBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookingsByRoomId(String roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    @Transactional
    public Booking saveBooking(BookingRequest bookingRequest, User loggedInUser) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date must come before check-out date");
        }

        Room room = roomService.findOneAvailableRoomByRoomClass(bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(), bookingRequest.getRoomClassId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No rooms available for the selected dates"));
        RoomClass roomClass = room.getRoomClass();
        Booking booking = new Booking(bookingRequest, room, loggedInUser,
                calculateBookingAmount(roomClass, bookingRequest.getAddOns()));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No booking found with booking code :" + confirmationCode));

    }

    private double calculateBookingAmount(RoomClass roomClass, List<AddOn> addOns) {
        return roomClass.getBasePrice()
                + addOns.stream().mapToDouble(AddOn::getPrice).sum();
    }
}
