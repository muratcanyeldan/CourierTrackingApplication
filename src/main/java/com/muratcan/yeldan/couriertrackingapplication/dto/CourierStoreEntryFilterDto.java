package com.muratcan.yeldan.couriertrackingapplication.dto;

import java.util.UUID;

public record CourierStoreEntryFilterDto(UUID courierId, String storeName) {
}
