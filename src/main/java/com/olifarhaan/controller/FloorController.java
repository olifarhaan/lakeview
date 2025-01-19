package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Floor;
import com.olifarhaan.request.FloorRequest;
import com.olifarhaan.service.interfaces.IFloorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/floors")
public class FloorController {

    private final IFloorService floorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody FloorRequest floorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(floorService.createFloor(floorRequest));
    }

    @GetMapping
    public ResponseEntity<List<Floor>> getAllFloors() {
        return ResponseEntity.ok(floorService.getAllFloors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Floor> getFloorById(@PathVariable String id) {
        return ResponseEntity.ok(floorService.getFloorById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Floor> updateFloor(@PathVariable String id, @RequestBody FloorRequest floorRequest) {
        return ResponseEntity.ok(floorService.updateFloor(id, floorRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFloor(@PathVariable String id) {
        floorService.deleteFloor(id);
        return ResponseEntity.noContent().build();
    }
}
