package com.olifarhaan.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.olifarhaan.model.RoomClass;
import com.olifarhaan.request.RoomClassRequest;
import com.olifarhaan.response.RoomClassResponse;

import jakarta.annotation.Nullable;

public interface IRoomClassService {
    List<RoomClass> getAllRoomClasses();

    RoomClass getRoomClassById(String roomClassId);

    List<RoomClassResponse> findByAvailability(LocalDate checkInDate, LocalDate checkOutDate,
            @Nullable String roomClassId);

    RoomClass createRoomClass(RoomClassRequest roomClassRequest);

    RoomClass updateRoomClass(String roomClassId, RoomClassRequest roomClassRequest);

    void deleteRoomClass(String roomClassId);
}
