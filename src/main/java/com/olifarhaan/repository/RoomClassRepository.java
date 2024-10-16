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
    // @Query(value = """
    // SELECT rc.id as id, rc.title as title, rc.description as description,
    // rc.base_price as basePrice, COUNT(r.id) as availableRooms
    // FROM room_classes rc
    // LEFT JOIN rooms r ON r.room_class_id = rc.id
    // AND r.room_status = 'AVAILABLE'
    // AND NOT EXISTS (
    // SELECT 1 FROM bookings br
    // WHERE br.room_id = r.id
    // AND br.booking_status NOT IN ('CONFIRMED', 'CHECKED_IN')
    // AND (
    // (:checkInDate BETWEEN br.check_in_date AND br.check_out_date)
    // OR (:checkOutDate BETWEEN br.check_in_date AND br.check_out_date)
    // )
    // )
    // WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
    // GROUP BY rc.id, rc.title, rc.description, rc.base_price
    // """, nativeQuery = true)
    // List<RoomClassResponse> findRoomClassAvailability(
    // @Param("checkInDate") LocalDate checkInDate,
    // @Param("checkOutDate") LocalDate checkOutDate,
    // @Nullable @Param("roomClassId") String roomClassId);
    // @Query(value = """
    // SELECT new com.olifarhaan.response.RoomClassResponse(
    // rc.id,
    // rc.title,
    // rc.description,
    // rc.basePrice,
    // 12,
    // rc.features,
    // rc.bedTypes,
    // rc.images
    // ) FROM RoomClass rc
    // LEFT JOIN Room r ON r.roomClass.id = rc.id
    // AND r.roomStatus = 'AVAILABLE'
    // AND NOT EXISTS (
    // SELECT 1 FROM Booking br
    // WHERE br.room.id = r.id
    // AND br.bookingStatus NOT IN ('CONFIRMED', 'CHECKED_IN')
    // AND (
    // (:checkInDate BETWEEN br.checkInDate AND br.checkOutDate)
    // OR (:checkOutDate BETWEEN br.checkInDate AND br.checkOutDate)
    // )
    // )
    // WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
    // GROUP BY rc.id, rc.title, rc.description, rc.base_price, rc.features,
    // rc.bedTypes, rc.images
    // """)
    // List<RoomClassResponse> findRoomClassAvailability(
    // @Param("checkInDate") LocalDate checkInDate,
    // @Param("checkOutDate") LocalDate checkOutDate,
    // @Nullable @Param("roomClassId") String roomClassId);

    // @Query(value = """
    // SELECT rc.id AS id,
    // rc.title AS title,
    // rc.description AS description,
    // rc.base_price AS basePrice,
    // COUNT(r.id) AS availableRooms,
    // GROUP_CONCAT(DISTINCT f.feature) AS features, -- Assuming feature_name is the
    // column in the features table
    // GROUP_CONCAT(DISTINCT bt.bed_type) AS bedTypes, -- Assuming bed_type_name is
    // the column in the bed types table
    // GROUP_CONCAT(DISTINCT img.image_url) AS images -- Assuming image_url is the
    // column in the images table
    // FROM room_classes rc
    // LEFT JOIN rooms r ON r.room_class_id = rc.id
    // AND r.room_status = 'AVAILABLE'
    // AND NOT EXISTS (
    // SELECT 1 FROM bookings br
    // WHERE br.room_id = r.id
    // AND br.booking_status NOT IN ('CONFIRMED', 'CHECKED_IN')
    // AND (
    // (:checkInDate BETWEEN br.check_in_date AND br.check_out_date)
    // OR (:checkOutDate BETWEEN br.check_in_date AND br.check_out_date)
    // )
    // )
    // LEFT JOIN room_class_feature f ON f.room_class_id = rc.id -- Join for
    // features
    // LEFT JOIN room_class_bed_type bt ON bt.room_class_id = rc.id -- Join for bed
    // types
    // LEFT JOIN room_class_image img ON img.room_class_id = rc.id -- Join for
    // images
    // WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
    // GROUP BY rc.id, rc.title, rc.description, rc.base_price
    // """, nativeQuery = true)
    // List<RoomClassResponse> findRoomClassAvailability(
    // @Param("checkInDate") LocalDate checkInDate,
    // @Param("checkOutDate") LocalDate checkOutDate,
    // @Nullable @Param("roomClassId") String roomClassId);

    // @Query("""
    // SELECT NEW com.olifarhaan.response.RoomClassResponse(
    // rc,
    // COUNT(r)
    // )
    // FROM RoomClass rc
    // LEFT JOIN Room r ON r.roomClass = rc
    // AND r.roomStatus = 'AVAILABLE'
    // AND NOT EXISTS (
    // SELECT 1 FROM Booking b
    // WHERE b.room = r
    // AND b.bookingStatus NOT IN ('CONFIRMED', 'CHECKED_IN')
    // AND (
    // (:checkInDate BETWEEN b.checkInDate AND b.checkOutDate)
    // OR (:checkOutDate BETWEEN b.checkInDate AND b.checkOutDate)
    // )
    // )
    // WHERE (:roomClassId IS NULL OR rc.id = :roomClassId)
    // AND (:guestCount IS NULL OR rc.maxGuestCount >= :guestCount)
    // GROUP BY rc
    // """)
    // List<RoomClassResponse> findRoomClassAvailability(
    // @Param("checkInDate") LocalDate checkInDate,
    // @Param("checkOutDate") LocalDate checkOutDate,
    // @Nullable @Param("roomClassId") String roomClassId,
    // @Param("guestCount") int guestCount);

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
