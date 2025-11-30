package com.muratcan.yeldan.couriertrackingapplication.controller;

import com.muratcan.yeldan.couriertrackingapplication.dto.request.CourierLocationRequestDto;
import com.muratcan.yeldan.couriertrackingapplication.repository.CourierRepository;
import com.muratcan.yeldan.couriertrackingapplication.repository.CourierStoreEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CourierControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.1-alpine");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.4.0-alpine"))
            .withExposedPorts(6379);
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private CourierStoreEntryRepository courierStoreEntryRepository;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        courierStoreEntryRepository.deleteAll();
        courierRepository.deleteAll();
    }

    @Test
    void processLocation_ShouldReturnCreated_WhenCourierEntersStore() {
        UUID courierId = UUID.randomUUID();
        CourierLocationRequestDto request = new CourierLocationRequestDto(
                LocalDateTime.now(),
                courierId,
                40.9923307,
                29.1244229);

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/courier/location", request, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, courierStoreEntryRepository.count());
    }

    @Test
    void processLocation_ShouldReturnOk_WhenCourierMovesWithoutEntry() {
        UUID courierId = UUID.randomUUID();
        CourierLocationRequestDto request = new CourierLocationRequestDto(
                LocalDateTime.now(),
                courierId,
                41.0,
                29.0);

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/courier/location", request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, courierStoreEntryRepository.count());
    }

    @Test
    void getTotalTravelDistance_ShouldReturnCorrectDistance() {
        UUID courierId = UUID.randomUUID();

        CourierLocationRequestDto req1 = new CourierLocationRequestDto(
                LocalDateTime.now(), courierId, 40.0, 29.0);
        ResponseEntity<Void> res1 = restTemplate.postForEntity("/v1/courier/location", req1, Void.class);
        assertEquals(HttpStatus.OK, res1.getStatusCode());

        CourierLocationRequestDto req2 = new CourierLocationRequestDto(
                LocalDateTime.now(), courierId, 40.1, 29.0);
        ResponseEntity<Void> res2 = restTemplate.postForEntity("/v1/courier/location", req2, Void.class);
        assertEquals(HttpStatus.OK, res2.getStatusCode());

        ResponseEntity<String> response = restTemplate.getForEntity("/v1/courier/total-distance/" + courierId,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("km"));
    }
}
