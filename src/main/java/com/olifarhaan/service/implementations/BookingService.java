package com.olifarhaan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.domains.BookingStatus;
import com.olifarhaan.model.Booking;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.BookingRepository;
import com.olifarhaan.request.BookingRequest;
import com.olifarhaan.response.RoomWithBasePrice;
import com.olifarhaan.service.interfaces.IBookingService;
import com.olifarhaan.service.interfaces.IRoomService;

import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final RedissonClient redissonClient;

    private static final String ROOM_LOCK_PREFIX = "room_lock:";
    private static final int LOCK_WAIT_TIME = 0;
    private static final int LOCK_LEASE_TIME = 10;
    private final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Override
    public List<Booking> getAllBookings(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            logger.debug("Getting all bookings for userId: {}", userId);
            return bookingRepository.findByUserId(userId);
        }
        logger.debug("Getting all bookings");
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(String bookingId) {
        logger.debug("Getting booking with id: {}", bookingId);
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("No booking found with booking id :%s", bookingId)));
    }

    @Override
    public void cancelBooking(String bookingId) {
        logger.debug("Cancelling booking with id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookingsByRoomId(String roomId) {
        logger.debug("Getting all bookings by room id: {}", roomId);
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public Booking saveBooking(BookingRequest bookingRequest, User loggedInUser) {
        logger.debug("Saving booking for user with id: {}", loggedInUser.getId());
        List<RoomWithBasePrice> availableRooms = roomService
                .findAllAvailableRoomsByRoomClass(bookingRequest.getCheckInDate(),
                        bookingRequest.getCheckOutDate(), bookingRequest.getRoomClassId());

        for (RoomWithBasePrice roomWithBasePrice : availableRooms) {
            String roomId = roomWithBasePrice.room().getId();
            String lockKey = generateLockKey(roomId, bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
            logger.debug("Generating lock key for room id: {}", roomId);

            RLock lock = redissonClient.getFairLock(lockKey);
            boolean isLocked = false;

            try {
                isLocked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);
                if (!isLocked) {
                    logger.debug("Failed to lock room with id: {}", roomId);
                    continue;
                }

                boolean isRoomBooked = bookingRepository.getRoomBooking(roomId, bookingRequest.getCheckInDate(),
                        bookingRequest.getCheckOutDate()).isPresent();
                if (!isRoomBooked) {
                    logger.debug("Room with id: {} is not booked", roomId);
                    Booking booking = new Booking(bookingRequest, roomWithBasePrice.room(), loggedInUser);
                    Booking savedBooking = bookingRepository.save(booking);
                    bookingRepository.flush();
                    return savedBooking;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Booking process interrupted");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Booking process interrupted.");
            } finally {
                if (isLocked && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    logger.debug("Unlocking room with id: {}", roomId);
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT,
                "All available rooms are currently being booked by other guests. Please try other dates.");
    }

    private String generateLockKey(String roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return String.format("%s:%s:%s:%s", ROOM_LOCK_PREFIX, roomId, checkInDate.toString(), checkOutDate.toString());
    }

    @Override
    public Booking findByBookingConfirmationCode(Long confirmationCode) {
        logger.debug("Finding booking by confirmation code: {}", confirmationCode);
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No booking found with booking code :" + confirmationCode));
    }
}
