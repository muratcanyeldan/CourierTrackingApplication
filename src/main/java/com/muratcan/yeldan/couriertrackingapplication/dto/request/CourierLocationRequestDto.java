package com.muratcan.yeldan.couriertrackingapplication.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourierLocationRequestDto(
        @NotNull(message = "time is required") LocalDateTime time,

        @NotNull(message = "courier ID is required") UUID courier,

        @NotNull(message = "latitude is required")
        @DecimalMin(value = "-85.0", message = "latitude must be >= -85.0")
        @DecimalMax(value = "85.0", message = "latitude must be <= 85.0")
        Double lat,

        @NotNull(message = "longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
        @DecimalMax(value = "180.0", message = "longitude must be <= 180")
        Double lng) {
}
