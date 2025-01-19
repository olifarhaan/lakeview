package com.olifarhaan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Override
    public Room createRoom(RoomRequest roomRequest) {
        logger.debug("Creating room");
        CompletableFuture<Floor> floorFuture = CompletableFuture
                .supplyAsync(() -> floorService.getFloorById(roomRequest.getFloorId()));
        CompletableFuture<RoomClass> roomClassFuture = CompletableFuture
                .supplyAsync(() -> roomClassService.getRoomClassById(roomRequest.getRoomClassId()));
        Room room = new Room(floorFuture.join(), roomClassFuture.join(), roomRequest.getRoomNumber(),
                roomRequest.getRoomStatus());
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        logger.debug("Getting all rooms");
        return roomRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void deleteRoom(String roomId) {
        logger.debug("Deleting room with id: {}", roomId);
        roomRepository.deleteById(roomId);
    }

    @Override
    public Room updateRoom(String roomId, RoomRequest roomRequest) {
        logger.debug("Updating room with id: {}", roomId);
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
        logger.debug("Getting room with id: {}", roomId);
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Room not found with id: %s", roomId)));
    }

    @Override
    public Optional<RoomWithBasePrice> findOneAvailableRoomByRoomClass(LocalDate checkInDate, LocalDate checkOutDate,
            String roomClassId) {
        logger.debug("Finding one available room by room class with checkInDate: {}, checkOutDate: {}, roomClassId: {}",
                checkInDate, checkOutDate, roomClassId);
        List<RoomWithBasePrice> roomWithBasePrices = roomRepository.findOneAvailableRoomByRoomClass(checkInDate,
                checkOutDate, roomClassId, Pageable.ofSize(1));
        return roomWithBasePrices.isEmpty() ? Optional.empty() : Optional.of(roomWithBasePrices.get(0));
    }

    @Override
    public List<RoomWithBasePrice> findAllAvailableRoomsByRoomClass(LocalDate checkInDate, LocalDate checkOutDate,
            String roomClassId) {
        logger.debug("Finding all available rooms by room class with checkInDate: {}, checkOutDate: {}, roomClassId: {}",
                checkInDate, checkOutDate, roomClassId);
        return roomRepository.findAllAvailableRoomsByRoomClass(checkInDate, checkOutDate, roomClassId,
                Pageable.unpaged());
    }
}
