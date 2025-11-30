package com.muratcan.yeldan.couriertrackingapplication.controller;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierStoreEntryResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierStoreEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/courier-store-entry")
@RequiredArgsConstructor
public class CourierStoreEntryController {

    private final CourierStoreEntryService courierStoreEntryService;

    @GetMapping
    public ResponseEntity<List<CourierStoreEntryResponseDto>> getStoreEntries(@RequestParam(name = "courier-id", required = false) UUID courierId,
                                                                              @RequestParam(name = "store-name", required = false) String storeName) {
        return ResponseEntity.ok(courierStoreEntryService.getCourierStoreEntries(courierId, storeName));
    }
}
