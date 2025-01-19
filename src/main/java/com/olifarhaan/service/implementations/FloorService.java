package com.olifarhaan.service.implementations;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Floor;
import com.olifarhaan.repository.FloorRepository;
import com.olifarhaan.request.FloorRequest;
import com.olifarhaan.service.interfaces.IFloorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloorService implements IFloorService {

    private final FloorRepository floorRepository;

    private final Logger logger = LoggerFactory.getLogger(FloorService.class);

    @Override
    public Floor createFloor(FloorRequest floorRequest) {
        logger.debug("Creating floor");
        return floorRepository.save(new Floor(floorRequest));
    }

    @Override
    public Floor getFloorById(String id) {
        logger.debug("Getting floor by id: {}", id);
        return floorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found with id: " + id));
    }

    @Override
    public List<Floor> getAllFloors() {
        logger.debug("Getting all floors");
        return floorRepository.findAll();
    }

    @Override
    public Floor updateFloor(String id, FloorRequest floorRequest) {
        logger.debug("Updating floor with id: {}", id);
        Floor floor = getFloorById(id);
        Optional.ofNullable(floorRequest.getName()).ifPresent(floor::setName);
        return floorRepository.save(floor);
    }

    @Override
    public void deleteFloor(String id) {
        logger.debug("Deleting floor with id: {}", id);
        floorRepository.deleteById(id);
    }
}
