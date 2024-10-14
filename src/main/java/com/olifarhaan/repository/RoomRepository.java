package com.olifarhaan.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.olifarhaan.model.Room;

/**
 * @author M. Ali Farhan
 */

public interface RoomRepository extends JpaRepository<Room, String> {
    @Query(value = """
            SELECT COUNT(*) FROM rooms r
            WHERE r.room_class_id = :roomClassId
            AND r.room_status_id = 'AVAILABLE'
            AND NOT EXISTS (
                SELECT 1 FROM bookings br
                WHERE br.room_id = r.id
                AND br.booking_status NOT IN ('CONFIRMED', 'CHECKED_IN')
                AND (
                    (:checkInDate BETWEEN br.check_in_date AND br.check_out_date)
                    OR (:checkOutDate BETWEEN br.check_in_date AND br.check_out_date)
                )
            )
            """, nativeQuery = true)
    long countAvailableRoomsByRoomClass(LocalDate checkInDate, LocalDate checkOutDate, String roomClassId);

    @Query(value = """
            SELECT r.* FROM rooms r
            WHERE r.room_class_id = :roomClassId
            AND r.room_status_id = 'AVAILABLE'
            AND NOT EXISTS (
                SELECT 1 FROM bookings br
                WHERE br.room_id = r.id
                AND br.booking_status NOT IN ('CONFIRMED', 'CHECKED_IN')
                AND (
                    (:checkInDate BETWEEN br.check_in_date AND br.check_out_date)
                    OR (:checkOutDate BETWEEN br.check_in_date AND br.check_out_date)
                )
            )
            LIMIT 1
            """, nativeQuery = true)
    Optional<Room> findOneAvailableRoomByRoomClass(LocalDate checkInDate, LocalDate checkOutDate, String roomClassId);

    List<Room> findAllByOrderByCreatedAtDesc();

}
