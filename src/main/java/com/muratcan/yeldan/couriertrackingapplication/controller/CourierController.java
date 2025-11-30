package com.muratcan.yeldan.couriertrackingapplication.controller;

import com.muratcan.yeldan.couriertrackingapplication.dto.request.CourierLocationRequestDto;
import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping("/location")
    public ResponseEntity<Void> processLocation(@RequestBody @Valid CourierLocationRequestDto request) {
        boolean created = courierService.processLocation(request);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/total-distance/{id}")
    public ResponseEntity<String> getTotalTravelDistance(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(courierService.getTotalTravelDistance(id) + " km");
    }

    @GetMapping
    public ResponseEntity<List<CourierResponseDto>> getAllCouriers() {
        return ResponseEntity.ok(courierService.getAllCouriers());
    }
}
