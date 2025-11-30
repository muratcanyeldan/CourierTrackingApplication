package com.muratcan.yeldan.couriertrackingapplication.service;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierStoreEntryResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;

import java.util.List;
import java.util.UUID;

public interface CourierStoreEntryService {

    List<CourierStoreEntryResponseDto> getCourierStoreEntries(UUID courierId, String storeName);

    void save(CourierStoreEntry courierStoreEntry);
}
