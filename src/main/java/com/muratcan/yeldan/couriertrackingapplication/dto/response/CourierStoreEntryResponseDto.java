package com.muratcan.yeldan.couriertrackingapplication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourierStoreEntryResponseDto(UUID courierId,
                                           String storeName,
                                           LocalDateTime timestamp,
                                           Double lat,
                                           Double lng) {
}
