package com.olifarhaan.request;

import com.olifarhaan.domains.RoomStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotBlank
    private String floorId;
    @NotBlank
    private String roomClassId;
    @NotBlank
    private String roomNumber;
    @NotNull
    private RoomStatus roomStatus;
}
