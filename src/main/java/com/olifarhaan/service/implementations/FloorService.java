package com.olifarhaan.service.implementations;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Floor createFloor(FloorRequest floorRequest) {
        return floorRepository.save(new Floor(floorRequest));
    }

    @Override
    public Floor getFloorById(String id) {
        return floorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found with id: " + id));
    }

    @Override
    public List<Floor> getAllFloors() {
        return floorRepository.findAll();
    }

    @Override
    public Floor updateFloor(String id, FloorRequest floorRequest) {
        Floor floor = getFloorById(id);
        Optional.ofNullable(floorRequest.getName()).ifPresent(floor::setName);
        return floorRepository.save(floor);
    }

    @Override
    public void deleteFloor(String id) {
        floorRepository.deleteById(id);
    }
}
