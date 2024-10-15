package com.olifarhaan.response;

import com.olifarhaan.model.RoomClass;

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
		RoomClass roomClass,
		Long availableRooms) {

}
