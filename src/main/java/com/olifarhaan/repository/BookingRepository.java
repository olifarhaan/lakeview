package com.olifarhaan.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.olifarhaan.model.Booking;

/**
 * @author M. Ali Farhan
 */

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByRoomId(String roomId);

    Optional<Booking> findByBookingConfirmationCode(Long confirmationCode);

    List<Booking> findByUserId(String userId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
            AND b.bookingStatus IN ('CONFIRMED', 'CHECKED_IN')
            AND (
                (:checkInDate BETWEEN b.checkInDate AND b.checkOutDate)
                OR (:checkOutDate BETWEEN b.checkInDate AND b.checkOutDate)
            )
            """)
    Optional<Booking> getRoomBooking(
            @Param("roomId") String roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);
}
