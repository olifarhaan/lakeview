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
    @Query(value = """
            SELECT new com.olifarhaan.response.RoomClassResponse(
                rc.id,
                rc.basePrice,
                rc.title,
                rc.description,
                COUNT(r.id),
                rc.features,
                rc.bedTypes,
                rc.images
            ) FROM RoomClass rc
            LEFT JOIN Room r ON r.roomClass.id = rc.id
            WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
            AND r.roomStatus.id = 'AVAILABLE'
            AND NOT EXISTS (
                SELECT 1 FROM bookings br
                WHERE br.room.id = r.id
                AND br.bookingStatus NOT IN ('CONFIRMED', 'CHECKED_IN')
                AND (
                    (:checkInDate BETWEEN br.checkInDate AND br.checkOutDate)
                    OR (:checkOutDate BETWEEN br.checkInDate AND br.checkOutDate)
                )
            )
            GROUP BY rc.id, rc.basePrice, rc.title, rc.description, rc.features, rc.bedTypes, rc.images
            """)
    List<RoomClassResponse> findByAvailability(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Nullable @Param("roomClassId") String roomClassId);
}
