package com.muratcan.yeldan.couriertrackingapplication.service;

import com.muratcan.yeldan.couriertrackingapplication.dto.request.CourierLocationRequestDto;
import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierResponseDto;

import java.util.List;
import java.util.UUID;

public interface CourierService {

    boolean processLocation(CourierLocationRequestDto requestDto);

    double getTotalTravelDistance(UUID courierId);

    List<CourierResponseDto> getAllCouriers();
}
