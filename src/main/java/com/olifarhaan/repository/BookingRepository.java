package com.olifarhaan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olifarhaan.model.Booking;

/**
 * @author M. Ali Farhan
 */

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByRoomId(String roomId);

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByUserId(String userId);
}
