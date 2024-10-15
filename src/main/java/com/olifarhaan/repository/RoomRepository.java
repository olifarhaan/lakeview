package com.olifarhaan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.olifarhaan.model.Room;
import com.olifarhaan.response.RoomWithBasePrice;

/**
 * @author M. Ali Farhan
 */

public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findAllByOrderByCreatedAtDesc();

    @Query("""
            SELECT NEW com.olifarhaan.response.RoomWithBasePrice(r, rc.basePrice)
            FROM Room r
            JOIN r.roomClass rc
            WHERE r IN (
                SELECT r2 FROM Room r2
                WHERE r2.roomClass.id = :roomClassId
                AND r2.roomStatus = 'AVAILABLE'
                AND NOT EXISTS (
                    SELECT 1 FROM Booking b
                    WHERE b.room = r2
                    AND b.bookingStatus NOT IN ('CONFIRMED', 'CHECKED_IN')
                    AND (
                        (:checkInDate BETWEEN b.checkInDate AND b.checkOutDate)
                        OR (:checkOutDate BETWEEN b.checkInDate AND b.checkOutDate)
                    )
                )
            )
            """)
    List<RoomWithBasePrice> findOneAvailableRoomByRoomClass(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("roomClassId") String roomClassId,
            Pageable pageable);

}
