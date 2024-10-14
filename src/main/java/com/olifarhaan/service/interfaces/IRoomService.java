package com.olifarhaan.service.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.olifarhaan.model.Room;
import com.olifarhaan.request.RoomRequest;

/**
 * @author M. Ali Farhan
 */

public interface IRoomService {
    Room createRoom(RoomRequest roomRequest);

    List<Room> getAllRooms();

    void deleteRoom(String roomId);

    Room updateRoom(String roomId, RoomRequest roomRequest);

    Room getRoomById(String roomId);

    long countAvailableRoomsByRoomClass(LocalDate checkInDate, LocalDate checkOutDate, String roomClassId);

    Optional<Room> findOneAvailableRoomByRoomClass(LocalDate checkInDate, LocalDate checkOutDate, String roomClassId);
}
