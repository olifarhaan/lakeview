package com.olifarhaan.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FloorRequest {
    @NotBlank
    private String name;
}
