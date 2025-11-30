package com.muratcan.yeldan.couriertrackingapplication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourierResponseDto(UUID id,
                                 double totalDistance,
                                 Double lastLat,
                                 Double lastLng,
                                 LocalDateTime createDate) {
}
