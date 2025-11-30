package com.muratcan.yeldan.couriertrackingapplication.service;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.StoreResponseDto;

import java.util.List;

public interface StoreService {

    List<StoreResponseDto> getAllStores();

    StoreResponseDto getStoreByName(String name);
}
