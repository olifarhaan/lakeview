package com.olifarhaan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.olifarhaan.model.RoomClass;
import com.olifarhaan.response.RoomClassResponse;

import jakarta.annotation.Nullable;

public interface RoomClassRepository extends JpaRepository<RoomClass, String> {
   
    @Query("""
                SELECT NEW com.olifarhaan.response.RoomClassResponse(
                    rc,
                    (SELECT COUNT(r) FROM Room r
                     WHERE r.roomClass.id = rc.id
                     AND r.roomStatus = 'AVAILABLE'
                     AND NOT EXISTS (
                         SELECT 1 FROM Booking b
                         WHERE b.room.id = r.id
                         AND b.bookingStatus IN ('CONFIRMED', 'CHECKED_IN')
                         AND (
                             (:checkInDate >= b.checkInDate AND :checkInDate <= b.checkOutDate)
                             OR (:checkOutDate >= b.checkInDate AND :checkOutDate <= b.checkOutDate)
                         )
                     ))
                )
                FROM RoomClass rc
                WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
                AND (:guestCount IS NULL OR rc.maxGuestCount >= :guestCount)
                ORDER BY rc.createdAt ASC
            """)
    List<RoomClassResponse> findRoomClassAvailability(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Nullable @Param("roomClassId") String roomClassId,
            @Nullable @Param("guestCount") Integer guestCount);

}
