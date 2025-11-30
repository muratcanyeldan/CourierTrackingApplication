package com.muratcan.yeldan.couriertrackingapplication.controller;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.StoreResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{name}")
    public ResponseEntity<StoreResponseDto> getStoreByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(storeService.getStoreByName(name));
    }
}
