package com.olifarhaan.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Room;
import com.olifarhaan.request.RoomRequest;
import com.olifarhaan.service.interfaces.IRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final IRoomService roomService;

    @PostMapping
    public ResponseEntity<Room> addNewRoom(@Valid @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.createRoom(roomRequest));
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() throws SQLException {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable String roomId, @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomRequest));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
}
