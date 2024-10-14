package com.olifarhaan.response;

import java.util.List;

import com.olifarhaan.domains.BedType;
import com.olifarhaan.domains.Feature;

public record RoomClassResponse(
        String id,
        double basePrice,
        String title,
        String description,
        int availableRooms,
        List<Feature> features,
        List<BedType> bedTypes,
        List<String> images) {
}
