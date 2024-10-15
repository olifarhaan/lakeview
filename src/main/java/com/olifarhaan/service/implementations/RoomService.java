package com.olifarhaan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Floor;
import com.olifarhaan.model.Room;
import com.olifarhaan.model.RoomClass;
import com.olifarhaan.repository.RoomRepository;
import com.olifarhaan.request.RoomRequest;
import com.olifarhaan.response.RoomWithBasePrice;
import com.olifarhaan.service.interfaces.IFloorService;
import com.olifarhaan.service.interfaces.IRoomClassService;
import com.olifarhaan.service.interfaces.IRoomService;

import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final IFloorService floorService;
    private final IRoomClassService roomClassService;

    @Override
    public Room createRoom(RoomRequest roomRequest) {
        Floor floor = floorService.getFloorById(roomRequest.getFloorId());
        RoomClass roomClass = roomClassService.getRoomClassById(roomRequest.getRoomClassId());
        Room room = new Room(floor, roomClass, roomRequest.getRoomNumber(), roomRequest.getRoomStatus());
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public Room updateRoom(String roomId, RoomRequest roomRequest) {
        Room room = getRoomById(roomId);
        Optional.ofNullable(roomRequest.getFloorId()).ifPresent(floorId -> {
            Floor floor = floorService.getFloorById(floorId);
            room.setFloor(floor);
        });
        Optional.ofNullable(roomRequest.getRoomClassId()).ifPresent(roomClassId -> {
            RoomClass roomClass = roomClassService.getRoomClassById(roomClassId);
            room.setRoomClass(roomClass);
        });
        Optional.ofNullable(roomRequest.getRoomNumber()).ifPresent(room::setRoomNumber);
        Optional.ofNullable(roomRequest.getRoomStatus()).ifPresent(room::setRoomStatus);

        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    @Override
    public long countAvailableRoomsByRoomClass(LocalDate checkInDate, LocalDate checkOutDate, String roomClassId) {
        // return roomRepository.countAvailableRoomsByRoomClass(checkInDate,
        // checkOutDate, roomClassId);
        return 0;
    }

    @Override
    public Optional<RoomWithBasePrice> findOneAvailableRoomByRoomClass(LocalDate checkInDate, LocalDate checkOutDate,
            String roomClassId) {
        List<RoomWithBasePrice> roomWithBasePrices = roomRepository.findOneAvailableRoomByRoomClass(checkInDate,
                checkOutDate, roomClassId, Pageable.ofSize(1));
        return roomWithBasePrices.isEmpty() ? Optional.empty() : Optional.of(roomWithBasePrices.get(0));
    }

}
