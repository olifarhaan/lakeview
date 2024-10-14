package com.olifarhaan.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.RoomClass;
import com.olifarhaan.request.RoomClassRequest;
import com.olifarhaan.response.RoomClassResponse;
import com.olifarhaan.service.interfaces.IRoomClassService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room-classes")
public class RoomClassController {
    private final IRoomClassService roomClassService;

    @GetMapping
    public ResponseEntity<List<RoomClass>> getAllRoomClasses() {
        return ResponseEntity.ok(roomClassService.getAllRoomClasses());
    }

    @GetMapping("/{roomClassId}")
    public ResponseEntity<RoomClass> getRoomClassById(@PathVariable String roomClassId) {
        RoomClass roomClass = roomClassService.getRoomClassById(roomClassId);
        return ResponseEntity.ok(roomClass);
    }

    @PostMapping
    public ResponseEntity<RoomClass> createRoomClass(@Valid @RequestBody RoomClassRequest roomClassRequest) {
        return ResponseEntity.ok(roomClassService.createRoomClass(roomClassRequest));
    }

    @PutMapping("/{roomClassId}")
    public ResponseEntity<RoomClass> updateRoomClass(@PathVariable String roomClassId,
            @RequestBody RoomClassRequest roomClassRequest) {
        return ResponseEntity.ok(roomClassService.updateRoomClass(roomClassId, roomClassRequest));
    }

    @DeleteMapping("/{roomClassId}")
    public ResponseEntity<Void> deleteRoomClass(@PathVariable String roomClassId) {
        roomClassService.deleteRoomClass(roomClassId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByAvailability")
    public ResponseEntity<List<RoomClassResponse>> findByAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomClassId) {
        return ResponseEntity.ok(roomClassService.findByAvailability(checkInDate, checkOutDate, roomClassId));
    }
}
