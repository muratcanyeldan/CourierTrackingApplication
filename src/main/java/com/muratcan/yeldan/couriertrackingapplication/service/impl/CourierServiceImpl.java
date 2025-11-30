package com.muratcan.yeldan.couriertrackingapplication.service.impl;

import com.muratcan.yeldan.couriertrackingapplication.constant.Constants;
import com.muratcan.yeldan.couriertrackingapplication.dto.request.CourierLocationRequestDto;
import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.Courier;
import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;
import com.muratcan.yeldan.couriertrackingapplication.exception.CourierNotFoundException;
import com.muratcan.yeldan.couriertrackingapplication.mapper.CourierMapper;
import com.muratcan.yeldan.couriertrackingapplication.model.GeoPoint;
import com.muratcan.yeldan.couriertrackingapplication.repository.CourierRepository;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierService;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierStoreEntryService;
import com.muratcan.yeldan.couriertrackingapplication.service.strategy.DistanceStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final CourierStoreEntryService courierStoreEntryService;
    private final DistanceStrategy distanceStrategy;
    private final StringRedisTemplate redisTemplate;
    private final CourierMapper courierMapper;

    @Transactional
    public boolean processLocation(CourierLocationRequestDto requestDto) {
        Courier courier = courierRepository.findById(requestDto.courier())
                .orElseGet(() -> {
                    Courier newCourier = new Courier();
                    newCourier.setId(requestDto.courier());
                    newCourier.setTotalDistance(0.0);
                    return newCourier;
                });

        if (courier.getLastLat() != null && courier.getLastLng() != null) {
            GeoPoint from = new GeoPoint(courier.getLastLat(), courier.getLastLng());
            GeoPoint to = new GeoPoint(requestDto.lat(), requestDto.lng());

            double dist = distanceStrategy.calculateDistance(from, to);
            courier.setTotalDistance(courier.getTotalDistance() + dist);
        }

        courier.setLastLat(requestDto.lat());
        courier.setLastLng(requestDto.lng());
        courierRepository.saveAndFlush(courier);

        return checkStoreEntries(requestDto);
    }

    public double getTotalTravelDistance(UUID courierId) {

        Optional<Courier> courier = courierRepository.findById(courierId);

        if (courier.isEmpty()) {
            throw new CourierNotFoundException(courierId);
        }

        return courier.get().getTotalDistance();
    }

    @Override
    public List<CourierResponseDto> getAllCouriers() {
        return courierRepository.findAll().stream()
                .map(courierMapper::toDto)
                .toList();
    }

    private boolean checkStoreEntries(CourierLocationRequestDto requestDto) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius(Constants.STORES_GEO_KEY,
                        new Circle(new Point(requestDto.lng(), requestDto.lat()),
                                new Distance(Constants.STORE_RADIUS_METERS, RedisGeoCommands.DistanceUnit.METERS)));

        boolean anyCreated = false;

        if (results != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                String storeName = result.getContent().getName();
                boolean created = logStoreEntry(requestDto, storeName);
                if (created) {
                    anyCreated = true;
                }
            }
        }

        return anyCreated;
    }

    private boolean logStoreEntry(CourierLocationRequestDto requestDto, String storeName) {

        String courierEntryKey = buildCourierEntryKey(requestDto.courier(), storeName);

        Boolean firstTimeEntry = redisTemplate.opsForValue()
                .setIfAbsent(courierEntryKey, "", Duration.ofMinutes(Constants.LOG_PERIOD_BETWEEN_COURIER_ENTRY_MINUTES));

        if (!Boolean.TRUE.equals(firstTimeEntry)) {
            log.debug("Store entry exists within 1 minute for key {}", courierEntryKey);
            return false;
        }

        CourierStoreEntry entry = createCourierStoreEntry(requestDto, storeName);
        courierStoreEntryService.save(entry);
        log.info("Courier {} entered {} at {}", requestDto.courier(), storeName, requestDto.time());
        return true;
    }

    private CourierStoreEntry createCourierStoreEntry(CourierLocationRequestDto requestDto, String storeName) {
        CourierStoreEntry entry = new CourierStoreEntry();
        entry.setCourierId(requestDto.courier());
        entry.setStoreName(storeName);
        entry.setTimestamp(requestDto.time());
        entry.setLat(requestDto.lat());
        entry.setLng(requestDto.lng());
        return entry;
    }

    private String buildCourierEntryKey(UUID courierId, String storeName) {
        return String.format("%s:%s:%s", Constants.COURIER_ENTRY_KEY, courierId, storeName);
    }
}
