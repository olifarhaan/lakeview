package com.olifarhaan.service.interfaces;

import java.util.List;

import com.olifarhaan.model.Floor;
import com.olifarhaan.request.FloorRequest;

public interface IFloorService {
    Floor createFloor(FloorRequest floorRequest);

    Floor getFloorById(String id);

    List<Floor> getAllFloors();

    Floor updateFloor(String id, FloorRequest floorRequest);

    void deleteFloor(String id);
}
