package com.olifarhaan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.RoomClass;
import com.olifarhaan.repository.RoomClassRepository;
import com.olifarhaan.request.RoomClassRequest;
import com.olifarhaan.response.RoomClassResponse;
import com.olifarhaan.service.interfaces.IRoomClassService;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomClassService implements IRoomClassService {
    private final RoomClassRepository roomClassRepository;
    private final Logger logger = LoggerFactory.getLogger(RoomClassService.class);

    @Override
    public List<RoomClass> getAllRoomClasses() {
        return roomClassRepository.findAll();
    }

    @Override
    public RoomClass getRoomClassById(String roomClassId) {
        logger.debug("Getting room class by id: {}", roomClassId);
        return roomClassRepository.findById(roomClassId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room class not found"));
    }

    @Override
    public RoomClass createRoomClass(RoomClassRequest roomClassRequest) {
        logger.debug("Creating room class");
        return roomClassRepository.save(new RoomClass(roomClassRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomClassResponse> findByAvailability(LocalDate checkInDate, LocalDate checkOutDate,
            @Nullable String roomClassId, @Nullable Integer guestCount) {
        logger.debug(
                "Finding room class availability with checkInDate: {}, checkOutDate: {}, roomClassId: {}, guestCount: {}",
                checkInDate, checkOutDate, roomClassId, guestCount);
        return roomClassRepository.findRoomClassAvailability(checkInDate, checkOutDate, roomClassId, guestCount);
    }

    @Override
    public RoomClass updateRoomClass(String roomClassId, RoomClassRequest roomClassRequest) {
        logger.debug("Updating room class with id: {}", roomClassId);
        RoomClass roomClass = getRoomClassById(roomClassId);
        Optional.ofNullable(roomClassRequest.getTitle()).ifPresent(roomClass::setTitle);
        Optional.ofNullable(roomClassRequest.getDescription()).ifPresent(roomClass::setDescription);
        Optional.ofNullable(roomClassRequest.getBasePrice()).ifPresent(roomClass::setBasePrice);
        Optional.ofNullable(roomClassRequest.getImages()).ifPresent(roomClass::setImages);
        Optional.ofNullable(roomClassRequest.getFeatures())
                .filter(features -> !features.isEmpty())
                .ifPresent(roomClass::setFeatures);
        Optional.ofNullable(roomClassRequest.getBedTypes())
                .filter(bedTypes -> !bedTypes.isEmpty())
                .ifPresent(roomClass::setBedTypes);
        return roomClassRepository.save(roomClass);
    }

    @Override
    public void deleteRoomClass(String roomClassId) {
        logger.debug("Deleting room class with id: {}", roomClassId);
        roomClassRepository.deleteById(roomClassId);
    }
}
