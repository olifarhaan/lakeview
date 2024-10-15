package com.olifarhaan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<RoomClass> getAllRoomClasses() {
        return roomClassRepository.findAll();
    }

    @Override
    public RoomClass getRoomClassById(String roomClassId) {
        return roomClassRepository.findById(roomClassId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room class not found"));
    }

    @Override
    public RoomClass createRoomClass(RoomClassRequest roomClassRequest) {
        return roomClassRepository.save(new RoomClass(roomClassRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomClassResponse> findByAvailability(LocalDate checkInDate, LocalDate checkOutDate,
            @Nullable String roomClassId) {
        return roomClassRepository.findRoomClassAvailability(checkInDate, checkOutDate, roomClassId);
    }

    @Override
    public RoomClass updateRoomClass(String roomClassId, RoomClassRequest roomClassRequest) {
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
        roomClassRepository.deleteById(roomClassId);
    }
}
