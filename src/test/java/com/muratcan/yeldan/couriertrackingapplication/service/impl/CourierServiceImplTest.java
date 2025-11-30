package com.muratcan.yeldan.couriertrackingapplication.service.impl;

import com.muratcan.yeldan.couriertrackingapplication.constant.Constants;
import com.muratcan.yeldan.couriertrackingapplication.dto.request.CourierLocationRequestDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.Courier;
import com.muratcan.yeldan.couriertrackingapplication.mapper.CourierMapper;
import com.muratcan.yeldan.couriertrackingapplication.model.GeoPoint;
import com.muratcan.yeldan.couriertrackingapplication.repository.CourierRepository;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierStoreEntryService;
import com.muratcan.yeldan.couriertrackingapplication.service.strategy.DistanceStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceImplTest {

    @InjectMocks
    private CourierServiceImpl courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierStoreEntryService courierStoreEntryService;

    @Mock
    private DistanceStrategy distanceStrategy;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private GeoOperations<String, String> geoOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    void processLocation_ShouldCreateNewCourier_WhenCourierDoesNotExist() {
        UUID courierId = UUID.randomUUID();
        CourierLocationRequestDto request = new CourierLocationRequestDto(
                LocalDateTime.now(),
                courierId,
                40.0,
                29.0);

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());
        when(redisTemplate.opsForGeo()).thenReturn(geoOperations);
        when(geoOperations.radius(anyString(), any(Circle.class)))
                .thenReturn(new GeoResults<>(Collections.emptyList()));

        courierService.processLocation(request);

        verify(courierRepository)
                .saveAndFlush(argThat(c -> c.getId().equals(courierId) && c.getTotalDistance() == 0.0));
    }

    @Test
    void processLocation_ShouldUpdateDistance_WhenCourierExistsAndHasLastLocation() {
        UUID courierId = UUID.randomUUID();
        Courier existingCourier = new Courier();
        existingCourier.setId(courierId);
        existingCourier.setLastLat(40.0);
        existingCourier.setLastLng(29.0);
        existingCourier.setTotalDistance(10.0);

        CourierLocationRequestDto request = new CourierLocationRequestDto(
                LocalDateTime.now(),
                courierId,
                40.1,
                29.1);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(existingCourier));
        when(distanceStrategy.calculateDistance(any(GeoPoint.class), any(GeoPoint.class))).thenReturn(5.0);
        when(redisTemplate.opsForGeo()).thenReturn(geoOperations);
        when(geoOperations.radius(anyString(), any(Circle.class)))
                .thenReturn(new GeoResults<>(Collections.emptyList()));

        courierService.processLocation(request);

        assertEquals(15.0, existingCourier.getTotalDistance());
        verify(courierRepository).saveAndFlush(existingCourier);
    }

    @Test
    void processLocation_ShouldLogStoreEntry_WhenCourierIsNearStore() {
        UUID courierId = UUID.randomUUID();
        CourierLocationRequestDto request = new CourierLocationRequestDto(
                LocalDateTime.now(),
                courierId,
                40.0,
                29.0);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(new Courier()));
        when(redisTemplate.opsForGeo()).thenReturn(geoOperations);

        GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult = new GeoResult<>(
                new RedisGeoCommands.GeoLocation<>("Migros", new Point(29.0, 40.0)),
                new Distance(50, RedisGeoCommands.DistanceUnit.METERS));
        when(geoOperations.radius(eq(Constants.STORES_GEO_KEY), any(Circle.class)))
                .thenReturn(new GeoResults<>(Collections.singletonList(geoResult)));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any())).thenReturn(true);

        boolean result = courierService.processLocation(request);

        assertTrue(result);
        verify(courierStoreEntryService).save(any());
    }
}
