package com.olifarhaan.response;

import java.util.List;

import com.olifarhaan.domains.BedType;
import com.olifarhaan.domains.Feature;

// public interface RoomClassResponse {
// 	String getId();

// 	double getBasePrice();

// 	String getTitle();

// 	String getDescription();

// 	long getAvailableRooms();

// 	List<Feature> getFeatures();

// 	List<BedType> getBedTypes();

// 	List<String> getImages();
// }

public record RoomClassResponse(
		String id,
		String title,
		String description,
		double basePrice,
		long availableRooms,
		List<Feature> features,
		List<BedType> bedTypes,
		List<String> images) {

}
