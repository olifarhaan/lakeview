package com.olifarhaan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.repository.BookingRepository;
import com.olifarhaan.repository.FloorRepository;
import com.olifarhaan.repository.RoomClassRepository;
import com.olifarhaan.repository.RoomRepository;
import com.olifarhaan.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/*
 * This controller is used to clean up the database
 * It is used to reset the database for testing purposes,
 * It is not recommended to use this in production
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cleanup")
public class CleanUpController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomClassRepository roomClassRepository;
    private final FloorRepository floorRepository;

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> cleanUpDB() {
        userRepository.deleteAll();
        roomRepository.deleteAll();
        bookingRepository.deleteAll();
        roomClassRepository.deleteAll();
        floorRepository.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
